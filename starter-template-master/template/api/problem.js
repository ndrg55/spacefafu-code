import request from '@/utils/request'
const group_name = 'qa'
const api_name = 'problem'
export default {
    list(type,label,page,size){
        return request({
            url:`/${group_name}/${api_name}/${type}/${label}/${page}/${size}`,
            method: 'get'
        })
    },
    findOne(id){
        return request({
            url:`/${group_name}/${api_name}/${id}`,
            method: 'get'
        })
    },
    save(pojo){
        return request({
            url:`/${group_name}/${api_name}`,
            method:'post',
            data:pojo
        })
    },
    findMys(userid){
        return request({
            url:`/${group_name}/${api_name}/search/1/10`,
            method: 'post',
            data:{
                "userid":userid
            }
        })
    }

}