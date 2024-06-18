<template>
    <div class="login_container">
       <div class="login_box">
         <div style="margin:20px 0; text-align:center; font-size:24px"><b>登录</b></div>
         <!-- 用户名-->
         <el-form ref="LoginFormRef" :model="loginForm" :rules="LoginFormRules" >
           <el-form-item prop="username">
             <el-input size="medium" style="margin:10px 0px;width: 300px;margin-left:25px" v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
           </el-form-item>
         <!-- 密码-->
            <el-form-item prop="password">
              <el-input size="medium" style="margin:10px 0px;width: 300px;margin-left:25px" show-password v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
           </el-form-item>
           <div style="margin:10px 0; text-align:center">
             <el-button type="primary" size="small" @click="login" >登录</el-button>
             <el-button type="warning" size="small" @click="resetLoginForm">重置</el-button>
           </div>
         </el-form>
       </div>
     </div>
   </template>

   <script>
   import request from '../utils/request';
     export default {
       name: "Login",
       data() {
      return {
        loginForm: {
          username:'',
          password:''
        },
        LoginFormRules:{
          username:[
            { required: true, message: '请输入用户名', trigger: 'blur' },
          ],
          password:[
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        }
      }
    },


       methods:{
        resetLoginForm(){},
        login() {
      this.$refs['LoginFormRef'].validate(async (valid) => {
        if (valid) {
          try {
            const { username, password } = this.loginForm;
            console.log(process.env.VUE_APP_BASE_API);
            const response = await this.request.get("/login", {
              params: {
                userName: username,
                password: password
              }
            });
            const res = response;
            if (res.status === 200) {

              // 存储用户信息到浏览器缓存
              localStorage.setItem("user", JSON.stringify(res.data));
              this.$router.push("/home");
              this.$message.success("登录成功");
            } else {
              this.$message.error(res.msg);
            }
          } catch (error) {
            this.$message.error("登录失败，请稍后重试");
          }
        }
      });
    }

    }
     }
   </script>

   <style scoped>
     .login_container{
       background-color: white;
       height: 100%;
     }

     .login_box{
       width: 350px;
       height: 300px;
       background-color: #fff;
       border-radius: 3px;
       position: absolute;
       left: 50%;
       top: 50%;
       transform: translate(-50%,-50%)
     }
   </style>
