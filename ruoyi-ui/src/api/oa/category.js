import request from '@/utils/request'

// 查询知识分类列表
export function listCategory(query) {
  return request({
    url: '/oa/category/list',
    method: 'get',
    params: query
  })
}

// 查询知识分类详细
export function getCategory(categoryId) {
  return request({
    url: '/oa/category/' + categoryId,
    method: 'get'
  })
}

// 新增知识分类
export function addCategory(data) {
  return request({
    url: '/oa/category',
    method: 'post',
    data: data
  })
}

// 修改知识分类
export function updateCategory(data) {
  return request({
    url: '/oa/category',
    method: 'put',
    data: data
  })
}

// 删除知识分类
export function delCategory(categoryId) {
  return request({
    url: '/oa/category/' + categoryId,
    method: 'delete'
  })
}
