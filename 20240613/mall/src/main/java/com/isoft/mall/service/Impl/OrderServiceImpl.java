package com.isoft.mall.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.isoft.mall.common.Constant;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.filter.UserFilter;
import com.isoft.mall.model.mapper.CartMapper;
import com.isoft.mall.model.mapper.OrderItemMapper;
import com.isoft.mall.model.mapper.OrderMapper;
import com.isoft.mall.model.mapper.ProductMapper;
import com.isoft.mall.model.pojo.Cart;
import com.isoft.mall.model.pojo.Order;
import com.isoft.mall.model.pojo.OrderItem;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.request.CreateOrderReq;
import com.isoft.mall.service.CartService;
import com.isoft.mall.service.OrderService;
import com.isoft.mall.service.UserService;
import com.isoft.mall.util.OrderCodeFactory;
import com.isoft.mall.util.QRCodeGenerator;
import com.isoft.mall.vo.CartVo;
import com.isoft.mall.vo.OrderItemVO;
import com.isoft.mall.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shen
 * Order订单的业务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    CartService cartService;
    @Resource
    ProductMapper productMapper;
    @Resource
    CartMapper cartMapper;
    @Resource
    OrderMapper orderMapper;
    @Resource
    OrderItemMapper orderItemMapper;
    @Resource
    UserService userService;
    @Value("${file.upload.ip}")
    String ip;
    /**
     * 创建接口订单
     * 数据库事务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){
        //1、拿到用户Id
        Integer userId = UserFilter.currentUser.getId();
        //2、从购物车查找已经勾选的商品
        List<CartVo> cartVoList = cartService.list(userId);
        //临时存储
        ArrayList<CartVo> cartVoListTemp = new ArrayList<>();
        for (int i = 0; i < cartVoList.size(); i++) {
            CartVo cartVo =cartVoList.get(i);
            if (cartVo.getSelected().equals(Constant.Cart.SELECTED)){
                cartVoListTemp.add(cartVo);
            }
        }
        cartVoList=cartVoListTemp;
        //3、如果购物车已勾选为空，报错
         if (CollectionUtils.isEmpty(cartVoList)){
             throw new MallException(MallExceptionEnum.CART_EMPTY);
         }
        //4、判断商品是否存在，上下架状态，库存
        validSaleStatusAndStock(cartVoList);
        //5、把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVoListToOrderItemList(cartVoList);
        //6、扣库存
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem =orderItemList.get(i);
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock()-orderItem.getQuantity();
            if (stock<0){
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //7、把购物车中的已勾选商品删除
        cleanCart(cartVoList);
        //8、生成订单
        Order order = new Order();
        //9、生成订单号，有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        //0包邮
        order.setPostage(0);
        //1在线支付
        order.setPaymentType(1);
        //插入到order表
        orderMapper.insertSelective(order);
        //10、循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //11、把结果返回
        return orderNo;
    }

    /**
     * 将购物车选中的商品都加起来
     * @param orderItemList 通过这个参数可以获得一个一个的商品
     * @return 总金额
     */
    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice=0;
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem=orderItemList.get(i);
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 清除已经在购物车勾选的商品
     * @param cartVoList 遍历集合
     */
    private void cleanCart(List<CartVo> cartVoList) {
        for (int i = 0; i < cartVoList.size(); i++) {
            CartVo cartVo = cartVoList.get(i);
            cartMapper.deleteByPrimaryKey(cartVo.getId());
        }
    }

    /**
     * 把购物车对象转为订单item对象
     * @return orderItemList 订单对象
     */
    private List<OrderItem> cartVoListToOrderItemList(List<CartVo> cartVoList) {
        ArrayList<OrderItem> orderItemList=new ArrayList<>();
        for (int i = 0; i < cartVoList.size(); i++) {
            CartVo cartVo = cartVoList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVo.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVo.getProductName());
            orderItem.setProductImg(cartVo.getProductImage());
            orderItem.setUnitPrice(cartVo.getPrice());
            orderItem.setQuantity(cartVo.getQuantity());
            orderItem.setTotalPrice(cartVo.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }
    /**
     * 判断商品是否存在，上下架状态，库存
     * @param cartVoList
     */
    private void validSaleStatusAndStock(List<CartVo> cartVoList) {
        for (int i = 0; i < cartVoList.size(); i++) {
            CartVo cartVo =cartVoList.get(i);
            //1.查出商品
            Product product = productMapper.selectByPrimaryKey(cartVo.getProductId());
            //2.判断商品是否存在，是否上架
            //只用1和0，这种是偷懒，需要在Constant以代码枚举的形式，显示上下架
            if (product==null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
                throw new MallException(MallExceptionEnum.NO_SALE);
            }
            //3.判断商品的库存
            if (cartVo.getQuantity()>product.getStock()){
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    /**
     * 用户详情页
     * @param orderNo 订单号
     * @return 返回一个给前台展示的orderVO
     */
    @Override
    public OrderVO detail(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在，则报错
        if (order==null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //订单存在，则需要判断所属
        Integer userId=UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)){
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        //返回给用户看的，需要把order的内容转换成orderVO给用户看
        OrderVO orderVO=  getOrderVO(order);
        return orderVO;
    }

    /**
     * order的内容转换成orderVO给用户看的信息
     * @param order 查询到的全部订单信息
     * @return 拼装好的orderVO
     */
    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对应的orderItemVOList
        List<OrderItem> orderItemsList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        ArrayList<OrderItemVO> orderItemVOList = new ArrayList<>();
        //itli可以快速生成遍历集合
        for (int i = 0; i < orderItemsList.size(); i++) {
            OrderItem orderItem = orderItemsList.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            //将orderItemsList里面每一个orderItem的数据拷贝给orderItemVO
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        //使用枚举，把返回正确的消息，而不是code，然后赋值给OrderStatusName上
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }
    /**
     * 给用户看的购物车列表
     */
    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize){
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        //orderListToOrderVOList方法是将orderList转换成前台需要的OrderVO
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    /**
     * @param orderList 转换的order
     * @return 转换为orderVO的对象
     */
    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = getOrderVO(order);
            //经过这次循环，那么内容将被补充完整
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    /**
     * 取消订单
     * @param orderNo 订单编号
     */
    @Override
    public void cancel(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order==null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //验证用户身份
        //订单存在，则需要判断所属
        Integer userId=UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)){
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        if (order.getOrderStatus() .equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    /**
     * 生成支付用的二维码
     * @param orderNo 订单编号
     * @return
     */
    @Override
    public String qrcode(String orderNo){
        //1.获得ip,需要自己去配置，不能在request里面去拿
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
       /* try {
            //简单获取ip,不一定准确
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }*/
        String address= ip+":"+request.getLocalPort();
        //写在二维码上的信息
        String payUrl= "http://" +address+"pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350,Constant.FILE_UPLOAD_DIR+orderNo+".png");
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String pngAddress ="http://"+address+"/images/"+orderNo+".png";
        return pngAddress;
    }
    /**
     * 支付接口
     */
    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order==null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //支付前判断
        if (order.getOrderStatus()== Constant.OrderStatusEnum.NOT_PAID.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    /**
     * 给管理员看的购物车列表
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        //orderListToOrderVOList方法是将orderList转换成前台需要的OrderVO
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }
    /**
     * 管理员发货
     */
    @Override
    public void deliver(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order==null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //支付前判断
        if (order.getOrderStatus()== Constant.OrderStatusEnum.PAID.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }
    /**
     * 完结订单
     */
    @Override
    public void finish(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order==null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //如果是普通用户，就要校验订单的所属
        if (!userService.checkAdminRole(UserFilter.currentUser)
                && !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        //发货后可以完结订单
        //支付前判断
        if (order.getOrderStatus()== Constant.OrderStatusEnum.DELIVERED.getCode()){
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }
}
