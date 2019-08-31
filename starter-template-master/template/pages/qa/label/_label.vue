<template>
      <!--两列布局--> 
  <div class="wrapper qa-content"> 
   <div class="fl left-list"> 
    <div class="tab-content"> 
     <div id="index" class="tab-pane active"> 
      <div class="tab-bottom-line"> 
       <ul class="sui-nav nav-tabs"> 
        <li :class="type=='new'?'active':''"><a @click="type='new'">最新问题</a></li> 
        <li :class="type=='hot'?'active':''"><a @click="type='hot'">热门问题</a></li> 
        <li :class="type=='wait'?'active':''"><a @click="type='wait'">等待回应</a></li> 
       </ul> 
       <div class="qa-list" v-infinite-scroll="loadMore"> 
         <div class="tab-content"> 
         <div id="new" :class="'tab-pane '+(type=='new'?'active':'')">
          <ul class="detail-list"> 
           <li class="qa-item" v-for="(item,index) in newlist" :key="index"> 
            <div class="fl record"> 
             <div class="number"> 
              <div class="border useful"> 
               <p class="usenum">{{item.thumbup}}</p> 
               <p>{{item.solve=='1'?'求助':'热心'}}</p> 
              </div> 
              <div class="border answer"> 
               <p class="ansnum">{{item.reply}}</p> 
               <p>回答</p> 
              </div> 
             </div> 
            </div> 
            <div class="fl info"> 
             <div class="question"> 
              <!-- <p class="author"><span class="name">{{item.replyname}}</span><span>{{formate(item.replytime)}}</span>回答</p>  -->
              <p class="title">
                
              <router-link :to="'/qa/problem/'+item.id" >{{item.title}}</router-link>  
              </p> 
             </div> 
             <div class="other">  
               <ul class="fl sui-tag"> 
               <li>{{item.lname}}</li> 
         
              </ul> 
              <div class="fr brower"> 
               <p>来自 <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>| {{formate(item.createtime)}}  </p> 
              </div> 
             </div> 
            </div> 
            <div class="clearfix"></div> </li> 
 
          </ul> 
         </div> 
         <div id="hot" :class="'tab-pane '+(type=='hot'?'active':'')"> 
          <ul class="detail-list"> 
           <li class="qa-item" v-for="(item,index) in hotlist" :key="index"> 
            <div class="fl record"> 
             <div class="number"> 
              <div class="border useful"> 
               <p class="usenum">{{item.thumbup}}</p> 
               <p>{{item.solve=='1'?'求助':'热心'}}</p> 
              </div> 
              <div class="border answer"> 
               <p class="ansnum">{{item.reply}}</p> 
               <p>回答</p> 
              </div> 
             </div> 
            </div> 
            <div class="fl info"> 
             <div class="question"> 
              <!-- <p class="author"><span class="name">{{item.replyname}}</span><span>{{item.replytime}}</span>回答</p>  -->
              <p class="title"><router-link :to="'/qa/problem/'+item.id" >{{item.title}}</router-link>  </p> 
             </div> 
             <div class="other">  
               <ul class="fl sui-tag"> 
               <li>{{item.lname}}</li> 
         
              </ul> 
              <div class="fr brower"> 
               <p>来自 <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>| {{formate(item.createtime)}} </p> 
              </div> 
             </div> 
            </div> 
            <div class="clearfix"></div> </li> 
 
          </ul> 
         </div> 
         <div id="wait" :class="'tab-pane '+(type=='wait'?'active':'')"> 
          <ul class="detail-list"> 
           <li class="qa-item" v-for="(item,index) in waitlist" :key="index"> 
            <div class="fl record"> 
             <div class="number"> 
              <div class="border useful"> 
               <p class="usenum">{{item.thumbup}}</p> 
               <p>{{item.solve=='1'?'求助':'热心'}}</p> 
              </div> 
              <div class="border answer"> 
               <p class="ansnum">{{item.reply}}</p> 
               <p>回答</p> 
              </div> 
             </div> 
            </div> 
            <div class="fl info"> 
             <div class="question"> 
              <!-- <p class="author"><span class="name">{{item.replyname}}</span><span>{{item.replytime}}</span>回答</p>  -->
              <p class="title"><router-link :to="'/qa/problem/'+item.id" >{{item.title}}</router-link>  </p> 
             </div> 
             <div class="other">  
               <ul class="fl sui-tag"> 
               <li>{{item.lname}}</li> 
         
              </ul> 
              <div class="fr brower"> 
               <p>来自 <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>| {{formate(item.createtime)}} </p> 
              </div> 
             </div> 
            </div> 
            <div class="clearfix"></div> </li> 
 
          </ul> 
         </div> 
        </div> 
       </div> 
      </div> 
     </div> 
     <div id="php" class="tab-pane">
       php 
     </div> 
     <div id="js" class="tab-pane">
       Javascript 
     </div> 
     <div id="python" class="tab-pane">
       python 
     </div> 
     <div id="java" class="tab-pane">
       java 
     </div> 
    </div> 
   </div> 
   <div class="fl right-tag"> 
    <div class="block-btn"> 
     <p>今天，你需要帮助或者帮助他人吗?</p> 
     <router-link class="sui-btn btn-block btn-share" to="/qa/submit">发布</router-link>
    </div> 
    <div class="hot-tags"> 
     <div class="head"> 
      <h3 class="title">热门标签</h3> 
     </div> 
     <div class="tags"> 
      <!-- <ul class="sui-tag"> 
       <li>Php</li> 
       <li>Javascript</li> 
       <li>Gif</li> 
       <li>Java</li> 
       <li>C#</li> 
       <li>iOS</li> 
       <li>C++</li> 
      </ul>  -->
     </div> 
    </div> 
   </div> 
   <div class="clearfix"></div> 
  </div> 
</template>
<script>
import problemApi from '@/api/problem'
import axios from 'axios'
import {formatDate} from "@/utils/date";
export default {
  asyncData({params}){
    return axios.all([problemApi.list('newlist',params.label,1,10),
      problemApi.list('hotlist',params.label,1,10),
        problemApi.list('waitlist',params.label,1,10)  ] ).then( axios.spread(function(newlist,hotlist,waitlist ){
          return {
            newlist:newlist.data.data.rows,
            hotlist:hotlist.data.data.rows,
            waitlist:waitlist.data.data.rows,
            label:params.label
          }
        }))
  },
  data(){
    return {
      type:'new',
      page_new: 1,
      page_hot: 1,
      page_wait: 1,
      
    }  
  },
  methods:{
    loadMore(){
      if(this.type==='new'){
        this.page_new++
        problemApi.list('newlist',this.label,this.page_new,10).then( res=>{
          this.newlist=this.newlist.concat( res.data.data.rows )
        })
      }
      if(this.type==='hot'){
        this.page_hot++
        problemApi.list('hotlist',this.label,this.page_hot,10).then( res=>{
          this.hotlist=this.hotlist.concat( res.data.data.rows )
        })
      }
      if(this.type==='wait'){
        this.page_wait++
        problemApi.list('waitlist',this.label,this.page_wait,10).then( res=>{
          this.waitlist=this.waitlist.concat( res.data.data.rows )
        })
      }   


    },
    formate(oridate){
       return  formatDate(oridate);
    }
  }
}
</script>

<style scoped >
@import "~/assets/css/page-sj-qa-logined.css";

</style>