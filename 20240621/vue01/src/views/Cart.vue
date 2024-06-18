<template>
    <div>
        <el-row>
            <el-col :span="24">
                <el-card>
                    <div slot="header" class="clearfix">
                        <span>购物车</span>
                        <el-button style="float: right;" type="primary" size="mini"
                            @click="toggleSelection">全选/清空</el-button>
                    </div>
                    <el-table ref="cartTable" :data="cartList" style="width: 100%">
                        <el-table-column type="selection" width="55"></el-table-column>
                        <el-table-column type="index" label="序号"></el-table-column>
                        <el-table-column prop="productName" label="商品名称"></el-table-column>
                        <el-table-column prop="price" label="价格"></el-table-column>
                        <el-table-column label="数量">
                            <template slot-scope="scope">
                                <el-input-number v-model="scope.row.quantity" size="mini" :min="1"
                                    @change="updateTotalPrice(scope.row)"></el-input-number>
                            </template>
                        </el-table-column>
                        <el-table-column prop="totalPrice" label="总价"></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button type="danger" size="mini" @click="deleteItem(scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div style="margin-top: 20px; text-align: right;">
                        <el-button type="success" @click="checkout">结算</el-button>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
    name: 'Cart',
    data() {
        return {
            cartList: [],
            allSelected: false,
            dialogFormVisible: false,
            form: {
                productName: '',
                price: '',
                quantity: '',
                stock: ''
            }


        };
    },
    methods: {

        loadCart() {
            this.request.get("/cart/list", {
                params: {
                    userId: JSON.parse(localStorage.getItem("user")).id
                }
            }).then(res => {
                this.cartList = res.data;
            });
        },

        deleteItem(row) {
            const userId = JSON.parse(localStorage.getItem("user")).id;
            const productId = parseInt(row.productId);

            this.request.post("/cart/delete", null, {
                params: {
                    userId: userId,
                    productId: productId
                }
            }).then(res => {
                this.loadCart();

            }).catch(err => {
                console.error(err);
            });
        },
        selectAll() {
            this.$refs.cartTable.toggleAllSelection();

        },
        checkout() {
            const selectedItems = this.$refs.cartTable.selection;
            if (selectedItems.length === 0) {
                this.$message.warning('请先选择商品');
            } else {
                // TODO执行结算逻辑

                this.$message.success('结算成功');
            }
        },
        increaseQuantity(row) {
            row.quantity++;
            this.updateTotalPrice(row);
        },
        decreaseQuantity(row) {
            if (row.quantity > 1) {
                row.quantity--;
                this.updateTotalPrice(row);
            }
        },
        updateTotalPrice(row) {
            row.totalPrice = row.price * row.quantity;
        },

    },
    mounted() {
        this.loadCart();

    },
};
</script>

<style scoped></style>
