import request from '@/utils/request'
const group_name = 'qa'
const api_name = 'reply'
export default {
    search(searchmap,page,size){
        return request({
            url:`/${group_name}/${api_name}/search/${page}/${size}`,
            method: 'post',
            data:searchmap
        })
    },
    save(pojo){
        return request({
            url:`/${group_name}/${api_name}`,
            method: 'post',
            data:pojo
        })
    }

}