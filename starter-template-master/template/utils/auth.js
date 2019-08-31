import Cookies from 'js-cookie'
const TokenKey='User-Token'
const NameKey='User-Name'
const AvatarKey='User-Avatar'
const IdKey='User-Id'
/*
export default {
    setUser(token,name,avatar){
        Cookies.set(TokenKey,token)
        Cookies.set(NameKey,name)
        Cookies.set(AvatarKey,avatar)
    }
}
*/

export function setUser(token,name,avatar,id){
    Cookies.set(TokenKey,token)
    Cookies.set(NameKey,name)
    Cookies.set(AvatarKey,avatar)
    Cookies.set(IdKey,id)
}

export function getUser(){
    console.log(Cookies.get(AvatarKey))
    return {
        token: Cookies.get(TokenKey),
        name: Cookies.get(NameKey),
        avatar: Cookies.get(AvatarKey),
        userid:Cookies.get(IdKey)
    }
}

export function removeUser(){
    Cookies.remove(TokenKey)
    Cookies.remove(NameKey)
    Cookies.remove(AvatarKey)
    Cookies.remove(IdKey)
}