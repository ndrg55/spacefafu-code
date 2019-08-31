<template>
  <div class="wrapper loginsign">
    <div class="form" align="center">
      <h3 class="loginsign-title">注册新账号</h3>
      <form class="sui-form">
        <div class="control-group">
          <!-- <label for="inputname" class="control-label">名字</label>  -->
          <div class="controls">
            <input
              type="text"
              id="inputname"
              v-model="pojo.nickname"
              placeholder="您的昵称"
              class="input-xlarge"
            >
          </div>
        </div>
        <div class="different">
          <div class="radio-content">
            <div id="a1" class="phone">
              <div class="control-group number">
                <input
                  type="text"
                  v-model="pojo.mobile"
                  placeholder="11位手机号"
                  class="input-xlarge"
                >
              </div>
              <div class="control-group code">
                <div class="input-append">
                  <input
                    id="appendedInputButton"
                    v-model="code"
                    type="text"
                    placeholder="短信验证"
                    class="span2 input-large msg-input"
                  >
                  <button v-show="show" type="button" @click="sendsms" class="sui-btn msg-btn">获取验证码</button>
                  <button v-show="!show" type="button" class="sui-btn msg-btn">请等待{{count}}秒</button>
                </div>
              </div>
              <div class="control-group">
                <!-- <label for="inputpassword" class="control-label">密码</label>  -->
                <div class="controls">
                  <input
                    type="password"
                    id="inputpassword"
                    v-model="pojo.password"
                    placeholder="请输入6-16位密码"
                    class="input-xlarge"
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="control-group btn-signup">
          <label class="control-label"></label>
          <div class="controls">
            <label>
              <!-- <input type="checkbox"> -->
              <!-- <span class="type-text" style="font-size:12px;">同意协议并接受《服务条款》</span> -->
            </label>
            <button type="button" class="sui-btn btn-danger btn-yes" @click="register()">注 册</button>
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
      password: "",
      show: true,
      count: "",
      timer: null
    };
  },
  methods: {
    getCode() {
      const TIME_COUNT = 60;

      if (!this.timer) {
        this.count = TIME_COUNT;

        this.show = false;

        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= TIME_COUNT) {
            this.count--;
          } else {
            this.show = true;

            clearInterval(this.timer);

            this.timer = null;
          }
        }, 1000);
      }
    },
    sendsms() {
      userApi.sendsms(this.pojo.mobile).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if(res.data.flag){

            this.getCode()
        }
      });
    },
    register() {
      //前端校验
      //前端校验
      if (this.pojo.nickname === undefined) {
         this.$message({
           message: "请输入昵称~",
           type: "warning"
         });
         return;
       }
      if (this.pojo.nickname.length <2||this.pojo.nickname.length>8) {
        this.$message({
          message: "请输入2-8位昵称",
          type: "warning"
        });
        return;
      }
       if (this.pojo.password === undefined) {
         this.$message({
           message: "请输入密码~",
           type: "warning"
         });
         return;
       }
      if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(this.pojo.mobile))){ 
        this.$message({
          message: "请输入正确的手机号~",
          type: "warning"
        });
        return ; 
      } 
      
      if (this.pojo.password.length <6||this.pojo.password.length>16) {
        this.$message({
          message: "请输入6-16位密码~",
          type: "warning"
        });
        return;
      }
      if (this.code === undefined||this.code.length!=6) {
         this.$message({
           message: "请输入6位验证码~",
           type: "warning"
         });
         return;
       }

      //注册
      userApi.register(this.pojo, this.code).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if (res.data.flag) {
          location.href = "/login";
        }
      });
    }
  }
};
</script>
<style scoped>
@import "~/assets/css/page-sj-person-loginsign.css";
</style>