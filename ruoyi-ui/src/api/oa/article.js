import request from '@/utils/request'

// 查询知识管理列表
export function listArticle(query) {
  return request({
    url: '/oa/article/listArticle',
    method: 'get',
    params: query
  })
}

// 查询知识管理详细
export function getArticle(articleId) {
  return request({
    url: '/oa/article/' + articleId,
    method: 'get'
  })
}

// 新增知识管理
export function addArticle(data) {
  return request({
    url: '/oa/article',
    method: 'post',
    data: data
  })
}

// 修改知识管理
export function updateArticle(data) {
  return request({
    url: '/oa/article',
    method: 'put',
    data: data
  })
}

// 删除知识管理
export function delArticle(articleId) {
  return request({
    url: '/oa/article/' + articleId,
    method: 'delete'
  })
}
