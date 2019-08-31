<template>
  <div>
    <header style="margin-bottom:10px;">
      <div class="wrapper">
        <div class="sui-navbar">
          <div class="navbar-inner">
            <a href="/" class="sui-brand">
              <img src="~/assets/img/logo_white02.jpg"  width="180px" height="53px" alt="logo">
            </a>
            <ul class="sui-nav">
              <router-link tag="li" to="/" active-class="active" exact>
                <a>首页</a>
              </router-link>
              <router-link tag="li" to="/qa" active-class="active">
                <a>互助</a>
              </router-link>
              <router-link tag="li" to="/gathering" active-class="active">
                <a>活动</a>
              </router-link>

              <router-link tag="li" to="/spit" active-class="active">
                <a>吐槽</a>
              </router-link>
            </ul>

            <!-- <form class="sui-form sui-form pull-left">
              <input type="text" v-model="searchinfo" placeholder="输入关键词...">
              <span class="btn-search fa fa-search" @click="search"></span>
            </form>-->
            <div class="search bar7">
              <form>
                <input type="text" v-model="searchinfo" placeholder="请输入.." name="crid">
                <button type="button" @click="search" onClick="submitAction();return false;"></button>
              </form>
            </div>
            <div class="sui-nav pull-left info" v-if="user.name!==undefined">
              <li>
                <img src="~/assets/img/message-icon.png" width="22px" height="22px">
              </li>
              <li style="margin-left:-15px">
                <a href="/notice" class="notice">
                  <span style="font-size:13px">通知</span>
                  <span class="messagepic" v-if="messagenum">{{messagenum}}</span>
                </a>
              </li>
              <li>
                <el-dropdown>
                  <span class="el-dropdown-link">
                    <a href="/manager" class="homego">
                      <img :src="user.avatar" width="35px" height="35px" :alt="user.name">
                    </a>
                    <i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="tomanager">
                      <a href="javascript:void(0);" class="homego">个人中心</a>
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="tofriendlist">
                      <a href="javascript:void(0);" class="homego">私聊({{unreadNum}})</a>
                    </el-dropdown-item>
                    <el-dropdown-item divided @click.native="logout">
                      <a href="javascript:void(0);" class="notice">退出</a>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </li>
            </div>
            <div class="sui-nav pull-left info" v-if="user.name===undefined">
              <li>
                <router-link to="/login">
                  <a class="notice">登陆</a>
                </router-link>
              </li>

              <li>
                <router-link to="/register">
                  <a class="notice">注册</a>
                </router-link>
              </li>
            </div>
          </div>
        </div>
      </div>
    </header>
    <nuxt/>
    <footer>
      <div class="footer" style="background-color:white">
        <div class="wrapper">
          <div class="footer-bottom">
            <div class="link">
              <dl>
                <img>
              </dl>
              <dl>
                <dt>网站相关</dt>
                <dd>关于我们</dd>
              </dl>

              <dl>
                <dt>联系我们</dt>
                <dd>联系我们</dd>
                <dd>加入我们</dd>
                <dd>建议反馈</dd>
              </dl>
              <dl>
                <dt>关于FaFu</dt>
                <dd>官方网站</dd>
                <dd>与我相关</dd>
              </dl>
            </div>
            <p class="Copyright">Copyright &copy; Space Fafu --寻华开发团队</p>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>
<script>
import "~/assets/plugins/normalize-css/normalize.css";
import "~/assets/plugins/yui/cssgrids-min.css";
import "~/assets/plugins/sui/sui.min.css";
import "~/assets/plugins/sui/sui-append.min.css";
import "~/assets/plugins/font-awesome/css/font-awesome.min.css";
import "~/assets/css/widget-base.css";
import "~/assets/css/widget-head-foot.css";

import { getUser, removeUser } from "@/utils/auth";
import userApi from "@/api/user";
import messagenumApi from "@/api/messagenum";
import chatlistApi from "@/api/chatlist";
export default {
  data() {
    return {
      user: {},
      searchinfo: "",
      messagenum: "",
      unreadNum:""
    };
  },
  mounted() {
    this.user = getUser();
    this.timer = -1;//首次设置小于0，立马调用getDatas函数
    this.intervalid=setInterval(() => {
      this.timer--;
      if(this.timer > 0){
      }else{
        this.getDatas();//调用getDatas函数
        this.timer = 20;//重置变量，60s后才能调用getDatas函数
      }
    },1000)
  },

  methods: {
    tomanager(){
      this.$router.push('/manager')
    },
    tofriendlist(){
        this.$router.push('/friendlist')
    },
    logout() {
      removeUser(); //清除登陆用户信息
      location.href = "/";
    },
    getDatas(){
      if (!(typeof (getUser().userid) == "undefined")) {
          messagenumApi.findbyuserid(getUser().userid).then(res => {
             this.messagenum=res.data.data.messagenum;

         
          });
           chatlistApi.getUnReadNum(getUser().userid).then(res => {
              this.unreadNum=res.data.data.unreadnum;
           });
      }
    },
    search() {
      if (this.searchinfo == "") {
        this.$message({
          showClose: true,
          message: "请输入关键字",
          type: "warning",
          duration: 500
        });
        return;
      }

      this.$router.push({
        name: "search",
        params: { searchinfo: this.searchinfo }
      });
      this.searchinfo = "";
    }
  },
  beforeDestroy(){
    clearInterval(this.intervalid);
  }
};
</script>
<style scoped>
.messagepic {
  padding: 0px 2px 0px 2px;
  font-size: 2px;
  border-radius: 5px;
  background: red;
  color: white;
  box-sizing: border-box;
  margin-left: 2px;
}
div.search {
  padding: 15px 0;
  float: left;

}

@media (min-width: 1200px) {
  div.search {
    margin-left: 240px;
    margin-right: 50px;
  }
}

@media only screen and (min-width: 1024px) and (max-width: 1200px) {
  div.search {
     margin-left: 140px;
    margin-right: 25px;
  }
}

@media (max-width: 1024px) {
  div.search {
     margin-left: 70px;
    margin-right: 10px;
  }
}
.search form {
  position: relative;
  width: 185px;
  margin: 0 auto;
}
.search input,
button {
  border: none;
  outline: none;
}
.search input {
  width: 100%;
  height: 29px;
  padding-left: 10px;
}
.search button {
  height: 38px;
  width: 42px;
  cursor: pointer;
  position: absolute;
}

/*搜索框7*/

.bar7 form {
  height: 42px;
}
.bar7 input {
  width: 135px;
  border-radius: 42px;
  border: 1px solid gainsboro;
  /* background: #f9f0da; */
  transition: 0.3s linear;
  float: right;
}
.bar7 input:focus {
  width: 185px;
}
.bar7 button {
  background: none;
  top: -2px;
  right: 20px;
}
.bar7 button:before {
  content: "Search";
  font-family: FontAwesome;
  color: #324b4e;
}
</style>
