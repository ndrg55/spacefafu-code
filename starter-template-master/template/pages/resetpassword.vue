<template>
  <div class="wrapper loginsign">
    <div class="form" align="center">
      <h3 class="loginsign-title">重置密码</h3>
      <form class="sui-form">
        <div class="control-group">
          <!-- <label for="inputname" class="control-label">名字</label>  -->
        </div>
        <div class="different">
          <div class="radio-content">
            <div id="a1" class="phone">
              <div class="control-group number">
                <input
                  type="text"
                  v-model="pojo.mobile"
                  placeholder="仅支持大陆手机号"
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
                  <button v-show="!show" type="button"  class="sui-btn msg-btn">请等待{{count}}秒</button>
                </div>
              </div>
              <div class="control-group">
                <!-- <label for="inputpassword" class="control-label">密码</label>  -->
              </div>
            </div>
          </div>
        </div>
        <div class="control-group btn-signup">
          <label class="control-label"></label>
          <div class="controls">
            <label>
              <span class="type-text" style="font-size:12px;">提示：成功后新密码为123456</span>
            </label>
            <button type="button" class="sui-btn btn-danger btn-yes" @click="reset()">重置</button>
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
    reset() {
      userApi.reset(this.pojo, this.code).then(res => {
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