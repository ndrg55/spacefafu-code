<template>
    <div>
     
        <div class="wrapper  myhome"> 
            <div class="left-list" style="background-color:white"> 
                <div class="myhome-list"> 
                <ul class="home-list"> 
                     <router-link to="/manager/account" tag="li" active-class="active"><a>个人资料</a></router-link>  
                
                </ul> 
                <ul class="home-list bottom"> 
                  <!-- <router-link to="/manager/myanswer" tag="li" active-class="active"><a>我的回答</a></router-link> -->
                  <router-link to="/manager/myquestion" tag="li" active-class="active"><a>我的互助</a></router-link>
                         
                </ul> 
                   <ul class="home-list"> 
                  <!-- <router-link to="/manager/myanswer" tag="li" active-class="active"><a>我的回答</a></router-link> -->
                  <router-link to="/manager/myspit" tag="li" active-class="active"><a>我的吐槽</a></router-link>
                         
                </ul> 
                </div> 
            </div> 
            <div class="right-content"> 
                <nuxt-child/>
            </div> 
            <div class="clearfix"></div> 
        </div> 
    </div>
</template>
<script>

import {getUser} from '@/utils/auth'
export default {
    beforeCreate(){
        if(getUser().name===undefined){
            this.$router.push('/login')
        }
    }
}
</script>
<style scoped>
@import '~/assets/css/page-sj-person-homepage.css';
</style>
