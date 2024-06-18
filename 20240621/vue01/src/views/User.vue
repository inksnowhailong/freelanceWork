<template>
    <div>
        <div style="padding:10px">
            <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入姓名搜索" v-model="username"></el-input>
            <el-input style="width:250px" suffix-icon="el-icon-emailAddress" placeholder="请输入邮箱搜索" v-model="emailAddress"></el-input>
            <el-input style="width:250px" suffix-icon="el-icon-position" placeholder="请输入地址搜索" v-model="address"></el-input>
            <el-button style="margin-left:5px" type="primary" @click="load">搜索</el-button>
            <el-button style="margin-left:5px" type="danger" @click="reset">重置</el-button>
          </div>
          <div style="margin:10px">
            <el-button type="primary"@click="handleAdd">新增<i class="el-icon-circle-plus"></i></el-button>
            <el-button type="primary">导入<i class="el-icon-bottom"></i></el-button>
            <el-button type="primary">导出<i class="el-icon-top"></i></el-button>
          </div>
          <el-table :data="tableData">
            <el-table-column prop="id" label="ID " width="80">
            </el-table-column>
            <el-table-column prop="username" label="姓名 " width="100">
            </el-table-column>
            <el-table-column prop="emailAddress" label="邮箱" width="180">
            </el-table-column>
            <el-table-column prop="phone" label="电话">
            </el-table-column>
            <el-table-column prop="nickName" label="昵称">
            </el-table-column>
            <el-table-column prop="address" label="地址">
            </el-table-column>
            <el-table-column prop="role" label="角色">
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="240">
              <template slot-scope="scope">
                <el-button type="success" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
                <el-popconfirm style="margin-left:5px"
                confirm-button-text='确定'
                cancel-button-text='再想想'
                icon="el-icon-info"
                icon-color="red"
                title="您确定删除吗？"
                @confirm="handleDelete(scope.row.id)"
              >
              <el-button type="danger" size="small" slot="reference" icon="el-icon-delete" >删除</el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <div style="padding:10px">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
              :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
          </div>
          <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
          <el-form label-width="80px" size="small">
            <el-form-item label="用户名">
              <el-input v-model="form.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="form.nickName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.emailAddress" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话">
              <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="角色">
              <el-select v-model="form.role" placeholder="请选择">
                <el-option label="普通用户" value="普通用户"></el-option>
                <el-option label="管理员" value="管理员"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="insert">确 定</el-button>

          </div>
        </el-dialog>
    </div>
</template>
<script>
export default {
    name: 'User',

    data(){
        return {
          tableData:[],
          total:0,
          pageNum:1,
          pageSize:5,
          username:"",
          emailAddress:"",
          nickname:"",
          address:"",
          dialogFormVisible:false,
          form:{}
        }
    },

    methods: {
      reset() {
        this.username = '';
        this.emailAddress = '';
        this.address = '';
        // 在这里调用 load 方法重新加载数据
        this.load();
    },
      load() {
          //请求分页查询数据
            //fetch("/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"").then(res=>res.json()).then(res=>{
            //使用axios封装的request

             this.request.get("/user/page",{
              params:{
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                username: this.username,
                emailAddress:this.emailAddress,
                address:this.address
              }
            }).then(res=>{

            this.tableData=res.records
            //如果role是2，显示管理员，否则显示普通用户
            this.tableData.forEach(item=>{
              if(item.role==2){
                item.role="管理员"
              }else{
                item.role="普通用户"
              }
            })
            this.total=res.total
            })
          },
          handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageSize = val;  //获取当前每页显示条数
        this.load();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageNum = val;   //获取当前第几页
        this.load();
      },

      handleAdd()
    {
      this.dialogFormVisible=true
      this.form={}
    },

    insert(){
          //将role变成数字
          if(this.form.role=="管理员"){
            this.form.role=2
          }else{
            this.form.role=1
          }
          //保存数据
          this.request.post("/user/save",this.form).then(res=>{
            if(res){
              this.$message.success("保存成功");
              this.dialogFormVisible=false;
              this.load();
            }else{
              this.$message.error("保存失败");
            }
          })
        },
        handleEdit(row){
          this.form=row;//把当前行的数据赋值给form
          this.dialogFormVisible=true;
        },
        handleDelete(id){
      this.request.delete("/user/"+id+"").then(res=>{
          if(res){
              this.$message.success("删除成功");
              this.load();
          }else{
              this.$message.error("删除失败");
          }
      })
  },

    },
    created() {
      //请求分页查询数据
      this.load();
    }

  }
  </script>
