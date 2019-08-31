import request from '@/utils/request'
import {getUser} from '@/utils/auth'
const group_name = 'user'
const api_name = 'user'

export default {
    sendsms(mobile){
        return request({
            url:`/${group_name}/${api_name}/sendsms/${mobile}`,
            method: 'post'
        })
    },
    register(user,code){
        return request({
            url:`/${group_name}/${api_name}/register/${code}`,
            method: 'post',
            data:user
        })
    },
    login(mobile,password){
        return request({
            url:`/${group_name}/${api_name}/login`,
            method: 'post',
            data: {
                mobile,
                password
            }
        })
    },
    info(id){
          return request({
           url: `/${group_name}/${api_name}/${id}`,
           method: 'get'
       }) 
    },
    saveinfo(user){
        return request({
            url: `/${group_name}/${api_name}/saveinfo`,
            method: 'put',
            data: user
        })
    },
    updateinfo(user){
        return request({
            url: `/${group_name}/${api_name}/${user.id}`,
            method: 'put',
            data: user
        })
    },
    reset(user,code){
        return request({
            url:`/${group_name}/${api_name}/reset/${code}`,
            method: 'post',
            data:user
        })
    },
    updatepassword(passw){
        return request({
            url:`/${group_name}/${api_name}/password`,
            method: 'put',
            data:passw
        })

    }
}