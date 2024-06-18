import request from '@/utils/request'

// 获取退货列表
export function refundList (data) {
  return request({
    url: '/refund/list',
    method: 'post',
    data
  })
}

// 添加退货
export function refundAdd (data) {
  return request({
    url: '/refund/addrefund',
    method: 'post',
    data
  })
}

// 根据商品id加载商品信息
export function BynameGood (commid) {
  return request({
    url: '/good/loadGoodById',
    method: 'post',
    params: {
      commid
    }
  })
}

// 下拉框显示数据
export function AllGood () {
  return request({
    url: '/good/AllGood',
    method: 'post'
  })
}
