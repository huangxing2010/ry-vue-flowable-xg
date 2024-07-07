import request from "@/utils/request";

// 工作台数据请求接口
export function getOwnCount(){
  return request({
    url: '/workflow/ownListCount',
    method: 'get'
  })
}

export function getFinishedCount(){
  return request({
    url: '/workflow/finishedCount',
    method: 'get'
  })
}

export function getTodoListCount(){
  return request({
    url: '/workflow/todoListCount',
    method: 'get'
  })
}
