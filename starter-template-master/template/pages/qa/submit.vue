<template>
  <div class="wrapper">
    <div class="qa-submit-form">
      <form action class="sui-form">
        <div class="input-title">
          <input type="text" v-model="reply.title" placeholder="标题：一句话说清问题，用问号结尾" class="input-xfat input-xxlarge title">
        </div>
        <div class="tags-area">
          <!-- <div class="input-tags"> -->
          <!-- <input
              type="text"
              placeholder="标签，如:php可使用逗号，分号； 来分隔"
              class="input-xfat input-xxlarge"
              id="tags"
          >-->
          <select v-model="reply.solve">
            <option value="1">求助</option>
            <option value="0">我只是热心~</option>
          </select>
          <select v-model="labelinfo">
            <option  
              v-for="(label,index) in labelList"
              :key="index"
              :value="{'lid':label.id,'lname':label.labelname}">{{label.labelname}}</option>
          </select>
          <!-- </div> -->
        </div>

        <div class="editor">
          <div
            class="quill-editor"
            :content="reply.content"
            @change="onEditorChange($event)"
            @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)"
            @ready="onEditorReady($event)"
            v-quill:myQuillEditor="editorOption"
          ></div>
        </div>
        <div class="submit">
          <!-- <span>已保存草稿</span> -->
          <span>
            <a class="sui-btn btn-release"  @click="save">发布互助</a>
          </span>
        </div>
        <div class="clearfix"></div>
      </form>
    </div>
  </div>
</template>
<script>
import "~/assets/css/page-sj-qa-submit.css";
import labelApi from "@/api/label";
import problemApi from "@/api/problem";
import { quillRedefine } from "vue-quill-editor-upload";
import { getUser } from "@/utils/auth";
export default {
  data() {
    return {
      labelinfo:{},
      reply: { solve: "1",content: "", }
    };
  },
  asyncData() {
    return labelApi.toplist().then(res => {
     
      return { labelList: res.data.data };
    });
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
      //判断用户是否登陆
      if (getUser().name === undefined) {
        this.$message({
          message: "请先登陆~",
          type: "warning"
        });
        this.$router.push('/login')
        return;
      }
  },
  methods: {
    onEditorBlur(editor) {
      console.log("editor blur!", editor);
    },
    onEditorFocus(editor) {
      console.log("editor focus!", editor);
    },
    onEditorReady(editor) {
      console.log("editor ready!", editor);
    },
    onEditorChange({ editor, html, text }) {
      console.log("editor change!", editor, html, text);
      this.reply.content = html;
    },
    save(){
        
        this.reply.lname=this.labelinfo.lname;
        this.reply.lid=this.labelinfo.lid;
        this.reply.userid=getUser().userid;
       

        //前端校验
        if (this.reply.title === undefined) {
          this.$message({
            message: "请输入标题~",
            type: "warning"
          });
          return;
        }
        if ( this.reply.lid === undefined) {
          this.$message({
            message: "请选择分类~",
            type: "warning"
          });
          return;
        }
        if (this.reply.content === '') {
          this.$message({
            message: "请输入内容~",
            type: "warning"
          });
          return;
        }
        //  console.log(JSON.stringify(this.reply));
        problemApi.save(this.reply).then(res=>{
            this.$message({
                  message: res.data.message,
                  type: (res.data.flag?'success':'error')
              })
              if(res.data.flag){
                  this.$router.push('/qa')
              }
        });
    },
   
    
  }
};
</script>
<style scoped >
@import "~/assets/css/page-sj-qa-submit.css";

</style>
