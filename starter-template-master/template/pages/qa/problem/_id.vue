<template>
  <!--两列布局-->
  <div class="wrapper qa-content">
    <div class="qa-title">
      <div class="fl title">
        <h2>{{problem.title}}</h2>
        <p>
          <span class="tag">{{problem.solve=='1'?'求助':'热心'}}</span>
          <span class="author"><a :href="'/user/persondata/'+problem.user.id"> {{problem.user.nickname}}</a></span>
          <span>{{ formate(problem.createtime)}}发布</span>
        </p>
      </div>
      <div class="fr share"></div>
      <div class="clearfix"></div>
    </div>
    <div class="fl left-list">
      <div class="problem-detail">
        <div class="title-intro">
          <div class="sidebar"></div>
          <div class="title">
            <div class="detail" v-html="problem.content"></div>
            <div class="clearfix"></div>
            <div class="operate">
              <!-- <span class="time">1小时前提问</span> -->
              <span class="comment" @click="childreply('0',problem.user)">评论</span>
              <!-- <span class="edit">编辑</span>
              <span class="jubao">举报</span>-->
            </div>
          </div>
        </div>
        <div class="answer-intro">
          <h4 class="answer-num">{{problem.reply}}个回答</h4>
          <h5 class="answer-num">点击内容可进行回复</h5>

          <div class="sidebar"></div>
          <!-- 评论区 -->
        <div class="comment-area"  v-infinite-scroll="loadMore">
          <div class="comment-tit">
            <span>评论</span>
          </div>
          <ul class="comment-list">
            <li v-for="(parentitem,parentindex) in replylist" :key="parentindex">
              <div class="item-photo"  @click="childreply(parentitem.id,parentitem.user)">
                <img :src="parentitem.user.avatar" alt width="50px" height="50px">
              </div>
              <div class="item-content" @click="childreply(parentitem.id,parentitem.user)">
                <p class="author">
                  <a :href="'/user/persondata/'+parentitem.user.id"> {{parentitem.user.nickname}}</a>&nbsp;{{formate(parentitem.createtime)}}
                </p>
                <p class="content" v-html="parentitem.content"></p>
              </div>
              <ul class="comment-list">
                <li v-for="(item,index) in parentitem.childReplys" :key="index">
                  <div class="item-photo"  @click="childreply(parentitem.id,item.user)">
                    <img :src="item.user.avatar" alt width="50px" height="50px">
                  </div>
                  <div class="item-content"  @click="childreply(parentitem.id,item.user)">
                    <p class="author">
                     <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>@回复 <a :href="'/user/persondata/'+item.targetUser.id"> {{item.targetUser.nickname}}</a> &nbsp;{{formate(item.createtime)}}
                    </p>
                    <p class="content" v-html="item.content">@回复</p>
                  </div>
                  <div class="item-thumb">
                    <div>
                      <!-- <i class="fa fa-thumbs-o-up" aria-hidden="true"></i> {{item.thumbup}} -->
                    </div>
                  </div>
                </li>
              </ul>
            </li>
          </ul>
        </div>
         
          <div class="clearfix"></div>
        </div>
      </div>
      <!-- <script>
    $(function () {
        $(".comment").click(function () {
            $(this).parents(".operate").siblings(".comment-box").toggle();
        });
        $(".sidebar .fa").hover(function () {
            $(this).css("color", "#e64620");
        }, function () {
            $(this).css("color", "#000");
        })
    })

      </script>-->
    </div>
    <!-- <div class="fl right-tag">
      <div class="similar-problem">
        <h3 class="title">相似问题</h3>
         <ul class="problem-list">
          <li class="list-item">
            <p class="list-title">求一份浏览器中html css javascript jquery ajax的渲染顺序与原理！！</p>
            <p class="list-info">
              <a href>1 回答</a> | 已解决
            </p>
          </li>
         
        </ul>
      </div>
    </div> -->
    <div class="clearfix"></div>
    <el-dialog :title="'评论'+ctargetname" :visible.sync="dialogVisible" width="40%">
      <div
        class="quill-editor"
        :content="ccontent"
        @change="onEditorChange($event)"
        v-quill:myQuillEditor="editorOption"
      ></div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>

import problemApi from "@/api/problem";
import replyApi from "@/api/reply";
import axios from "axios";
import { formatDate } from "@/utils/date";
import { getUser } from "@/utils/auth";
import {quillRedefine} from 'vue-quill-editor-upload'
export default {
  data() {
    return {
      dialogVisible: false,
      ccontent: "",
      cparentid: "",
      ctargetname: "",
      targetuserid:"",
      replypojo: {},
      pageNo:1,
      editorOption: {
        // some quill options
        modules: {
          toolbar: [
            [{ size: ["small", false, "large"] }],
            ["bold", "italic"],
            [{ list: "ordered" }, { list: "bullet" }],
            ["link", "image"]
          ]
        }
      }
    };
  },
  created(){
      this.editorOption=quillRedefine({
        uploadConfig:{
          action: 'http://localhost:9887/upload',
          res: (response) =>{
            
            return response.info
          },
          name: 'img'  //图片上传参数 
        }
      })
    },
  asyncData({ params }) {
    return axios
      .all([
        problemApi.findOne(params.id),
        replyApi.search({ problemid: params.id, parentid: "0" }, 1, 5)
      ])
      .then(
        axios.spread(function(problem, replylist) {
          return {
            problem: problem.data.data,
            replylist: replylist.data.data.rows,
            curruntproblemid: params.id
          };
        })
      );
  },
  methods: {
    loadMore(){
        this.pageNo++
        replyApi.search({ problemid: this.$route.params.id, parentid: "0" },this.pageNo,5).then( res => {
          this.replylist = this.replylist.concat( res.data.data.rows )
        })
      },
    formate(oridate) {
      return formatDate(oridate);
    },
    onEditorChange({ editor, html, text }) {
      this.ccontent = html;
    },
    childreply(parentid, targetuser) {

      //判断用户是否登陆
      if (getUser().name === undefined) {
        this.$message({
          message: "登陆之后才能评论~",
          type: "warning"
        });

        return;
      }
      this.targetuserid=targetuser.id
      this.cparentid = parentid;
      this.ctargetname = targetuser.nickname;
      this.dialogVisible = true;
      this.ccontent = "";
    },
    save() {
      this.replypojo = {
        problemid: this.curruntproblemid,
        content: this.ccontent,
        userid: getUser().userid,
        parentid: this.cparentid,
        targetuserid:this.targetuserid
      };
      replyApi.save(this.replypojo).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        })
        if (res.data.flag) {
          this.dialogVisible = false; //关闭窗口
          //刷新数据
          location.reload();
        }
      });
    },
    
  }
};
</script>
<style scoped >
@import "~/assets/css/page-sj-qa-detail.css";
.quill-editor {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}
</style>

