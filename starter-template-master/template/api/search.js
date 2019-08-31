import request from '@/utils/request'
const group_name = 'search'
const api_name = 'communion'
export default {
    search(keyword,page,size){
        return request({
            url:`/${group_name}/${api_name}/search/${keyword}/${page}/${size}`,
            method: 'get',
        })
    }, 
    searchByType(keyword,page,size,type){
        return request({
            url:`/${group_name}/${api_name}/search/${keyword}/${page}/${size}/${type}`,
            method: 'get',
        })
    },

}