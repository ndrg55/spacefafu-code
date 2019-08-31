import request from '@/utils/request'
const group_name = 'chat'
const api_name = 'chatinfo'
export default {
    getchat(userid,touserid,page,size){
        return request({
            url:`/${group_name}/${api_name}/${userid}/${touserid}/${page}/${size}`,
            method: 'get',
        })
    }, 
   
}