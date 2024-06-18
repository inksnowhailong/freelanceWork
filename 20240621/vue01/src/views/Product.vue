<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-input v-model="searchName" suffix-icon="el-icon-search" placeholder="搜索名称"></el-input>
            </el-col>
            <el-col :span="8">
                <el-button type="primary" @click="searchProducts">搜索</el-button>
                <el-button style="margin-left:5px" @click="reset">重置</el-button>
            </el-col>

        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
  <el-col :span="8" v-for="product in products" :key="product.id">
    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ product.name }}</span>
        <template v-if="product.status === 2">
          <div style="display: inline-block; margin-left: 80px; background-color: #67c23a; color: white; padding: 3px 10px; border-radius: 20px;">促销</div>
        </template>
      </div>
      <el-image
        :src="product.image"
        style="width: 100%; height: 200px; object-fit: cover;"
        fit="cover"
        @click="showImage(product.image)"
      ></el-image>
      <div style="margin: 15px 0;">
        <div><strong>详情:</strong> {{ product.detail }}</div>
        <div><strong>价格(元):</strong> {{ product.price }}</div>
        <div><strong>库存:</strong> {{ product.stock }}</div>
        <div><strong>上架时间:</strong> {{ product.createTime }}</div>
        <div><strong>保质期:</strong> {{ product.useTime }}</div>
      </div>
      <el-button type="success" size="small" icon="el-icon-edit" @click="showProduct(product)">加入购物车</el-button>
    </el-card>
  </el-col>
</el-row>

    <div style="padding: 10px;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
    <el-dialog :visible.sync="imageDialogVisible" width="50%">
      <img :src="dialogImageUrl" width="100%" alt>
    </el-dialog>


<!-- 商品的信息，可以选择商品数量 -->
<el-dialog title="商品信息" :visible.sync="dialogFormVisible">
    <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
            <el-input v-model="form.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="商品价格">
            <el-input v-model="form.price" disabled></el-input>
        </el-form-item>
        <el-form-item label="商品库存">
            <el-input v-model="form.stock" disabled></el-input>
        </el-form-item>
        <el-form-item label="购买数量">
            <el-input-number v-model="form.buyCount" :min="1" :max="form.stock" label="购买数量"></el-input-number>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCart(form)">确 定</el-button>
    </div>
</el-dialog>


    </div>
</template>

<script>
export default {
    name: 'Warehouses',
    data() {
        return {
            products: [],
            searchName: '',
            searchStatus: '',
            dialogFormVisible: false,
            form: {
                name: '',
                price: '',
                stock: '',
                status: '',
                buyCount: 1
            },
            pageNum: 1,
            pageSize: 5,
            total: 0,
            fileList: [],
            dialogVisible: false,
            dialogImageUrl: '',
            imageDialogVisible: false

        };
    },
    computed: {

    },
    methods: {
        reset() {
        this.searchName = '';
        this.searchStatus = '';
        // 在这里调用 load 方法重新加载数据
        this.searchProducts();
    },

        searchProducts() {
            this.request.get("/product/cuslist", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.searchName,

                }
            }).then(res => {
                this.products = res.data.list;
                //图片地址
                this.products.forEach(item => {
                    item.image = "" + item.image;
                });
                this.total = res.data.total;
            });
        },

        addCart(product) {
            var user = localStorage.getItem("user");
            // 调用添加购物车接口
            this.request.post("/cart/add", {
                userId: JSON.parse(user).id,
                productId: product.id,
                count: product.buyCount
            }).then(res => {
                if (res.status !== 200) {
                    this.$message({
                        message: '添加购物车失败',
                        type: 'error'
                    });
                    return;
                }else{
                    this.$message({
                        message: '添加购物车成功',
                        type: 'success'
                    });
                    this.dialogFormVisible = false;
                }

                this.dialogFormVisible = false;
            });
        },

        showProduct(product) {
            this.dialogFormVisible = true;
            this.form = product;
            this.form.buyCount = 1;
        },

        // 分页查询
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val;
            this.load();
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val;
            this.load();
        },

        handleSuccess(response, file, fileList) {
            console.log(response);
            console.log(file);
            console.log(fileList);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        showImage(imageUrl) {
        this.dialogImageUrl = imageUrl;
        this.imageDialogVisible = true;
        },
    },
    created() {
      //请求分页查询数据
      this.searchProducts();
    }

};
</script>

<style scoped>
.el-row {
    margin-bottom: 20px;
}
</style>
