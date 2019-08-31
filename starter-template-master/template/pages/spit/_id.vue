<template>
  <div class="wrapper tc-detail">
    <div class="fl left-list">
      <div class="tc-detail">
        <!-- 标题区 -->
        <div class="detail-tit">
          <div class="detail-author">
            <img :src="pojo.user.avatar" alt width="50px" height="50px">
             <a :href="'/user/persondata/'+pojo.user.id"> {{pojo.user.nickname}}</a> 发布 &nbsp;&nbsp;{{formate(pojo.publishtime)}}
          </div>
          <div class="detail-content">
            <p v-html="pojo.content"></p>
          </div>
          <div class="detail-tool">
            <ul>
              <li>
                <span class="star">
                  <i class="fa fa-thumbs-o-up" aria-hidden="true"></i>
                  {{pojo.thumbup}}
                </span>
              </li>
              <!-- <li><a href="#" ><i class="fa fa-share-alt" aria-hidden="true"></i> {{pojo.share}}</a></li>  -->
              <li>
                <a @click="spitDialog(pojo._id,pojo.userid)">
                  <i class="fa fa-commenting" aria-hidden="true"></i>
                 
                </a>
              </li>
            </ul>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comment-area"  v-infinite-scroll="loadMore">
          <div class="comment-tit">
            <span>评论</span>
          </div>
          <ul class="comment-list">
            <li v-for="(item,index) in commentlist" :key="index" >
              <div class="item-photo" @click="spitDialog(item._id,item.userid)">
                <img :src="item.user.avatar" alt width="50px" height="50px">
              </div>
              <div class="item-content" @click="spitDialog(item._id,item.userid)">
                <p class="author">
                  <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a> &nbsp;&nbsp; {{formate(item.publishtime)}}
                </p>
                <p class="content" v-html="item.content"></p>
              </div>
              <ul class="comment-list">
                <li v-for="(itemchild,index) in  item.childSpit" :key="index">
                  <div class="item-photo"  @click="spitDialog(item._id,itemchild.userid)">
                    <img :src="itemchild.user.avatar" alt width="50px" height="50px">
                  </div>
                  <div class="item-content"  @click="spitDialog(item._id,itemchild.userid)">
                    <p class="author">
                       <a :href="'/user/persondata/'+itemchild.user.id"> {{itemchild.user.nickname}}</a>@回复
                       <a :href="'/user/persondata/'+itemchild.targetUser.id"> {{itemchild.targetUser.nickname}}</a>&nbsp;&nbsp;{{formate(itemchild.publishtime)}}
                    </p>
                    <p class="content" v-html="itemchild.content">@回复</p>
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
      </div>
    </div>
    <div class="fl right-tag">
      <div class="block-btn">
        <p>有什么不满吗？说出来让自己好受点</p>
        <router-link class="sui-btn btn-block btn-share" to="/spit/submit">发吐槽</router-link>
      </div>
    </div>
    <div class="clearfix"></div>

    <el-dialog title="评论" :visible.sync="dialogVisible" width="40%">
      <div
        class="quill-editor"
        :content="content"
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
import spitApi from "@/api/spit";
import axios from "axios";
import { getUser } from "@/utils/auth";
import {formatDate} from "@/utils/date";
export default {
  asyncData({ params }) {
    return axios
      .all([spitApi.findById(params.id), spitApi.commentlist(params.id,1,5)])
      .then(
        axios.spread(function(pojo, commentlist) {
          return {
            pojo: pojo.data.data,
            commentlist: commentlist.data.data.rows
          };
        })
      );
  },
  data() {
    return {
      dialogVisible: false,
      content: "",
      parentid:"",
      nickname:"",
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
  methods: {
    loadMore(){
        this.pageNo++;
        spitApi.commentlist(this.$route.params.id,this.pageNo,5).then( res => {
          this.commentlist = this.commentlist.concat( res.data.data.rows )
        })
      },
    onEditorChange({ editor, html, text }) {
      this.content = html;
    },
    spitDialog(parentid,targetUserid){
       if (getUser().name === undefined) {
        this.$message({
          message: "未登陆",
          type: "warning"
        });

        return;
      }
      this.dialogVisible=true;
      this.content='';
      this.parentid=parentid;
      this.nickname=targetUserid;
    },
    save() {
     
      spitApi.save({
          content: this.content,
          parentid: this.parentid,
          userid: getUser().userid,
          nickname: this.nickname,
          state:this.pojo._id,
        })
        .then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.flag ? "success" : "error"
          });
          if (res.data.flag) {
            this.dialogVisible = false; //关闭窗口
            //刷新数据
            spitApi.commentlist(this.pojo._id,1,10).then(res => {
              this.commentlist = res.data.data.rows;
            });
          }
        });
    },
    formate(oridate){
       return  formatDate(oridate);
    }
  }
};
</script>

<style scoped >
@import "~/assets/css/page-sj-spit-detail.css";
.quill-editor {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}
</style>
