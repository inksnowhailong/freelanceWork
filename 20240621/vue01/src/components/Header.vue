<template>
    <el-dropdown style="width:100px; cursor:pointer">
        <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left:5px"></i>
        <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item>
              <span style="text-decoration: none" @click="logout">退出</span>
            </el-dropdown-item>
        </el-dropdown-menu>
    </el-dropdown>  
</template>
<script>
export default {
    name: "Header",
    props: {
    name: String
  },
  data() {
    return{
        user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{}
    }
  },
  methods:{
    logout(){
        this.$router.push("/login");
        localStorage.removeItem("user");
        this.$message.success("退出成功");
    }
  },
}
</script>

<style>

</style>
