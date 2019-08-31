<template>
  <div class="account" >
    <h4>
      <span>Ta的资料</span>
    </h4>
    <div class="infomain" style="padding:35px 10px 40px 150px">
      <div class="header">
        <img :src="user.avatar" alt class="head">
        
      </div>

      <div class="right_c">
        <div class="right_info">
          <div class="id_card">
            <span class="id_name">昵称：{{user.nickname}}</span>
            <!---->
            <!---->
            <a  class="user_home" @click="tochat()" href="javascript:void(0)" >私聊</a>
            <img  style="float:right;margin-top:4px;margin-right:2px;" width="10px" height="10px" src="~/assets/img/widget-edit.png">
            
            
            
          </div>
        </div>

        <div class="line"></div>

        <ul class="self">
          <!-- <li class="comon">手机号：{{user.mobile}}</li> -->
          <li class="comon">性别：{{user.sex=='1'?"男":"女"}}</li>
          <li class="comon">E-Mail：{{user.email}}</li>
          <li class="comon">兴趣：{{user.interest}}</li>
          <li class="intro">
            <span class="noWid">简介：</span>
            <span class="cont">{{user.personality}}</span>
          </li>
        </ul>
      </div>
    </div>
    

  </div>
</template>
<script>

import userApi from "@/api/user";
import { setUser, getUser, removeUser } from "@/utils/auth";
export default {
  /*asyncData(){
		return userApi.info().then(  res=>{
			return {user: res.data.data }
		})
	},*/
  data() {
    return {
        user:{},
     
    };
  },
  created() {
      userApi.info(this.$route.params.id).then(res => {
        this.user=res.data.data 
      });
  },

  methods: {
    
    tochat(){
      if (getUser().name === undefined) {
        this.$message({
          message: "请先登陆~",
          type: "warning"
        });
        // this.$router.push('/login')
        return;
      }
      location.href="/friendlist/chat/"+this.$route.params.id
    }
    
    
  }
};
</script>
<style scoped>
    @import "~/assets/css/mypersion.css";
</style>