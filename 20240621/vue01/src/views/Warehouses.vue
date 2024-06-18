<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-input v-model="searchName" suffix-icon="el-icon-search" placeholder="搜索名称"></el-input>
            </el-col>
            <el-col :span="8">
                <el-select v-model="searchStatus" suffix-icon="el-icon-search" placeholder="搜索状态">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="上架" value="1"></el-option>
                    <el-option label="下架" value="0"></el-option>
                    <el-option label="促销" value="2"></el-option>
                </el-select>
                <el-button type="primary" @click="searchProducts">搜索</el-button>
                <el-button style="margin-left:5px" @click="reset">重置</el-button>
            </el-col>


        </el-row>

        <el-button type="primary" @click="addProduct">新增<i class="el-icon-circle-plus"></i></el-button>

        <el-table :data="products" style="width: 100%; margin-top: 20px;">
            <el-table-column prop="id" label="ID" width="50"></el-table-column>
            <el-table-column prop="name" label="名称" width="150"></el-table-column>

            <el-table-column prop="image" label="图片" width="100">
                <template slot-scope="scope">
                    <el-popover placement="right" trigger="click">
                        <img :src="scope.row.image" alt="商品图片" width="200">
                        <img slot="reference" :src="scope.row.image" alt="商品图片" width="50" style="cursor: pointer;">
                    </el-popover>
                </template>
            </el-table-column>
            <!-- 内容过多时采用... -->
            <el-table-column prop="detail" label="详情"  width="350" show-overflow-tooltip ></el-table-column>
            <el-table-column prop="price" label="价格(元)" width="80"></el-table-column>
            <el-table-column prop="stock" label="库存" width="80"></el-table-column>

            <el-table-column prop="createTime" label="生产日期" width="180"></el-table-column>
            <el-table-column prop="useTime" label="保质期" width="150"></el-table-column>
            <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 1" type="success">上架</el-tag>
                <el-tag v-else-if="scope.row.status === 0" type="danger">下架</el-tag>
                <el-tag v-else-if="scope.row.status === 2" type="warning">促销</el-tag>
            </template>
            </el-table-column>

            <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                    <el-button type="success" size="small" icon="el-icon-edit"
                        @click="editProduct(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" slot="reference" icon="el-icon-delete"
                        @click="deleteProduct(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div style="padding:10px">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>
        <!-- 新增和修改的form -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" :rules="rules" ref="productForm" label-width="80px" size="small">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="详情" prop="detail">
          <el-input type="textarea" v-model="form.detail" :autosize="{ minRows: 3, maxRows: 10 }" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态">
        <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
            <el-radio :label="2">促销</el-radio>
        </el-radio-group>
        </el-form-item>

        <!-- 保质期,时间选择框 -->

          <!-- <el-date-picker
            v-model="form.useTime"
            type="date"
            placeholder="选择日期">
          </el-date-picker> -->
          <el-form-item label="保质期">
      <el-date-picker
        v-model="form.useTime"
        type="date"
        placeholder="选择日期"
        :picker-options="pickerOptions">
      </el-date-picker>
    </el-form-item>

        <el-form-item label="图片" prop="image">
          <el-upload
            class="upload-demo"
            :action="httpBase+'/upload/file'"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :onSuccess="handleSuccess"
            :file-list="fileList"
            :limit="1"
            list-type="picture-card">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible="dialogVisible" width="150px" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt>
          </el-dialog>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveProduct">确 定</el-button>
      </div>
    </el-dialog>

    </div>
</template>

<script>


export default {
    name: 'Warehouses',
    data() {
        return {
            httpBase:process.env.VUE_APP_BASE_API,
            products: [],
            dialogTitle: '新增',
            isEdit: false,
            searchName: '',
            searchStatus: '',
            dialogFormVisible: false,
            form: {
                name: '',
                image: '',
                detail: '',
                price: '',
                stock: '',
                status: '',
                useTime: null
            },
            pageNum: 1,
            pageSize: 5,
            total: 0,
            fileList: [],
            dialogVisible: false,
            dialogImageUrl: '',
            rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        detail: [
          { required: true, message: '请输入详情', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '请输入库存', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        useTime: [
          { required: true, message: '请选择保质期', trigger: 'change' }
        ]
      },
    //  禁用今天之前的日期
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 86400000;
        }
      },
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
            this.request.get("/product/list", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.searchName,
                    status: this.searchStatus
                }
            }).then(res => {
                this.products = res.data.list;
                //给图片加上前缀
                this.products.forEach(product => {
                    product.image = "/" + product.image;
                });
                this.total = res.data.total;
            });
        },
        addProduct() {
            this.dialogFormVisible = true;
            this.form = {
                name: '',
                image: '',
                detail: '',
                price: '',
                stock: '',
                status: ''
            };
            this.fileList = [];
            this.dialogTitle = '新增商品信息';
            this.isEdit = false;
        },
        editProduct(product) {
            this.dialogFormVisible = true;
            this.form = product;
            this.dialogTitle = '修改商品信息';
            this.isEdit = true;
            //图片回显
            this.fileList = [{
                url: product.image
            }];
        },
        deleteProduct(productId) {
    this.$confirm('确定删除该商品吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        this.products = this.products.filter(product => product.id !== productId);
        this.request.delete("/product/delete", {
            params: {
                id: productId
            }
        }).then(res => {
            if (res) {
                this.$message.success("删除成功");
                this.searchProducts();
            } else {
                this.$message.error("删除失败");
            }
        });
    }).catch(() => {
        this.$message.info('已取消删除');
    });
},

        // 执行保存接口的操作
        saveProduct() {
            // Logic to save the product
            this.dialogFormVisible = false;
            this.$refs.productForm.validate((valid) => {
                if (valid) {
                    // 执行保存操作
                    if(this.isEdit){
                        //更改form的status为上架或下架
                        this.request.post("/product/update", this.form).then(res => {
                            if (res) {
                                this.$message.success("修改成功");
                                this.searchProducts();
                            } else {
                                this.$message.error("修改失败");
                            }
                        });
                    }else{
                    this.request.post("/product/add", this.form).then(res => {
                        if (res.status === 200) {
                            this.$message.success("保存成功");
                            this.searchProducts();
                        } else {

                            this.$message.error(res.msg);
                        }
                    });
                }
                // this.searchProducts();
                this.dialogFormVisible = false;
                } else {
                    console.log('Form validation failed');
                    this.dialogFormVisible = true;
                    return false;
                }
            });
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

        handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file, fileList) {
      this.fileList = fileList;
    },
    handleSuccess(response, file, fileList) {
      this.form.image = response.data;
      this.fileList = fileList;
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
<style>

</style>
