<template>
  <div class="account">
    <h4>
      <span>个人资料</span>
    </h4>
    <div class="infomain">
      <div class="header">
        <img :src="user.avatar" alt class="head">
        <p class="modify" @click="updateavatarDialog">修改头像</p>
      </div>

      <div class="right_c">
        <div class="right_info">
          <div class="id_card">
            <span class="id_name">昵称：{{user.nickname}}</span>
            <!---->
            <!---->
            <a @click="updatepassword" class="user_home">修改密码</a>
            <a @click="updateinfoDialog" class="user_home">修改资料</a>
            
          </div>
        </div>

        <div class="line"></div>

        <ul class="self">
          <li class="comon">手机号：{{user.mobile}}</li>
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
    <el-dialog title="修改个人资料" :visible.sync="dialogVisible" width="40%">
      <el-form label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="pojo.nickname" placeholder="你的名称"></el-input>
        </el-form-item>
        <el-form-item label="E-Mail">
          <el-input v-model="pojo.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="兴趣">
          <el-input v-model="pojo.interest" placeholder="你的爱好~"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="pojo.personality" placeholder></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="pojo.sex" placeholder="请选择">
            <el-option key="1" label="男" value="1"></el-option>
            <el-option key="0" label="女" value="0"></el-option>
          </el-select>
        </el-form-item>

        <!-- <el-form-item>
          <el-button type="primary" @click="handleSave()">保存</el-button>
          <el-button @click="dialogFormVisible = false">关闭</el-button>
        </el-form-item>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">提交</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改头像" :visible.sync="dialogAvatar">
      <el-form label-width="80px">
        <div class="editor">
          <div
            class="quill-editor"
            :content="reply.content"
            @change="onEditorChange($event)"
            v-quill:myQuillEditor="editorOption"
          ></div>
        </div>
        
      </el-form>
      <span slot="footer" class="dialog-footer">
        
        <el-button @click="dialogAvatar = false">取消</el-button>
        <el-button type="primary" @click="SaveAvatar()">更改</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="dialogpassword" width="40%">
      <el-form label-width="80px">
        <el-form-item label="原密码">
          <el-input v-model="passw.oldpassword" type="password" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passw.newpassword" type="password" placeholder="请输入新的密码"></el-input>
        </el-form-item>
   
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogpassword = false">取 消</el-button>
        <el-button type="primary" @click="UpdatePassword">提交</el-button>
      </span>

    </el-dialog>
  </div>
</template>
<script>

import userApi from "@/api/user";
import { setUser, getUser, removeUser } from "@/utils/auth";
import axios from "axios";
import { quillRedefine } from "vue-quill-editor-upload";
export default {
  /*asyncData(){
		return userApi.info().then(  res=>{
			return {user: res.data.data }
		})
	},*/
  data() {
    return {
      reply: { solve: "1", content: "" },
      imageUrl: "",
      dialogVisible: false,
      dialogAvatar: false,
      dialogpassword:false,
      name_input: false,
      pojo: {},
      passw:{},
      user:{},
      editorOption: {
        // some quill options
        modules: {
          toolbar: [["image"]]
        }
      }
    };
  },
  beforeCreate(){
    //判断用户是否登陆
      if (getUser().name === undefined) {
        // this.$message({
        //   message: "请登陆",
        //   type: "warning"
        // });
        this.$router.push('/login')
        
      }
  },
  created() {
    
    this.editorOption = quillRedefine({
      uploadConfig: {
        action: "http://localhost:9887/upload",
        res: response => {
          return response.info;
        },
        name: "img" //图片上传参数
      }
    });
    userApi.info(getUser().userid).then(res => {
      this.user= res.data.data;
    });

  },
  // asyncData() {
  //   return 
  // },

  methods: {
    onEditorChange({ editor, html, text }) {
      // console.log('11111 change!', editor, html, text)

      var totalStr = html; //  introduce 是html代码
      //截取出图片  <img />
      var re = /<img[^>]+>/g;
      var a = totalStr.match(re);
      if (a != null) {
        //先清空
        this.reply = { solve: "1", content: "" };
        //回显最新的图片
        this.reply.content = a[a.length - 1];
      } else {
        //若不是图片 清空输入框
        this.reply = { solve: "1", content: "" };
      }
      // console.log('editor change!', editor, html, text)
    },

    updateinfoDialog() {
      this.dialogVisible = true;
      this.pojo = this.user;
    },
    updatepassword(){
      this.dialogpassword=true;
      this.passw={};
    },
    save() {
      userApi.updateinfo(this.pojo).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if (res.data.flag) {
          this.user = this.pojo;
          this.dialogVisible = false; //关闭窗口
          this.pojo = {};

          var token = getUser().token;
          var userid = getUser().userid;
          removeUser();
          setUser(token, this.user.nickname, this.user.avatar, userid);
          location.href = "/manager";
        }
      });
    },

    updateavatarDialog() {
      this.dialogAvatar = true;
    },
    SaveAvatar() {
      if (this.reply.content == "") {
        this.$message({
          message: "请选择图片",
          type: "error"
        });
      } else {
        // 取出img标签的src
        var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
        // 取引号的值
        var pattern = /".+"/;
        var src = this.reply.content.match(srcReg); //结果为[src="xxxx"]
        var urlyin = src[0].match(pattern)[0]; // 结果为"xxx"
        var url = urlyin.replace(/^\"|\"$/g, "");

        console.log(url);
        this.pojo = this.user;
        this.pojo.avatar = url;

        userApi.updateinfo(this.pojo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.flag ? "success" : "error"
          });
          if (res.data.flag) {
            this.user = this.pojo;
            this.dialogAvatar = false; //关闭窗口
            this.pojo = {};

            //更新cooike
             var token = getUser().token;
          var userid = getUser().userid;
          removeUser();
          setUser(token, this.user.nickname, this.user.avatar, userid);
          location.href = "/manager";
            location.href = "/manager";
          }
        });
      }
    },
    UpdatePassword(){
      this.passw.userid=getUser().userid;
      userApi.updatepassword(this.passw).then(res=>{
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if(res.data.flag){
          removeUser();
          location.href = "/login";
        }
      })
    }
  }
};
</script>
<style scoped>
@import "~/assets/css/mypersion.css";
</style>