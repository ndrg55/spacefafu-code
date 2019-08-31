<template>
  <div class="wrapper loginsign" >
    <div class="form" align="center">
      <h3 class="loginsign-title">用户登录</h3>
      <form class="sui-form login-form">
        <div class="control-group">
          <!-- <label for="inputname" class="control-label">手机号：</label>  -->
          <div class="controls">
            <input
              type="text"
              id="inputname"
              v-model="mobile"
              placeholder="11位手机号"
              class="input-xlarge"
              data-rules="required"
            >
          </div>
        </div>
        <div class="control-group">
          <!-- <label for="inputpassword" class="control-label">密码：</label>  -->
          <div class="controls">
            <input
              type="password"
              id="inputpassword"
              v-model="password"
              placeholder="输入登录密码"
              class="input-xlarge"
            >
          </div>
        </div>
        <div class="control-group btn-signup">
          <label class="control-label"></label>
          <div class="controls">
            <label>
              
              <a href="/resetpassword" class="type-text" style="font-size:12px;">忘记密码？</a>
            </label>
            <button type="button" class="sui-btn btn-danger btn-login" @click="login">登 录</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
<script>

import userApi from "@/api/user";
import { setUser, getUser } from "@/utils/auth";

export default {
  data() {
    return {
      pojo: {},
      code: "",
      mobile: "",
      password: ""
    };
  },
  methods: {
    sendsms() {
      userApi.sendsms(this.pojo.mobile).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
      });
    },
    register() {
      userApi.register(this.pojo, this.code).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
      });
    },
    login() {
      //前端校验
      if (this.mobile === '') {
        this.$message({
          message: "请输入手机号~",
          type: "warning"
        });
        return;
      }
       if (this.password === '') {
         this.$message({
           message: "请输入密码~",
           type: "warning"
         });
         return;
       }

      if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(this.mobile))){ 
        this.$message({
          message: "请输入正确的手机号~",
          type: "warning"
        });
        return ; 
      } 
      
      if (this.password.length <6||this.password.length>16) {
        this.$message({
          message: "请输入6-16位密码~",
          type: "warning"
        });
        return;
      }
        //登陆
      userApi.login(this.mobile, this.password).then(res => {
        if (res.data.flag) {
          //保存用户信息
          setUser(
            res.data.data.token,
            res.data.data.name,
            res.data.data.avatar,
            res.data.data.id            
          );

          location.href = "/manager"; //用户中心
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
          this.password = "";
          
        }
      });
    }
  }
};
</script>
<style scoped >
@import "~/assets/css/page-sj-person-loginsign.css";

</style>