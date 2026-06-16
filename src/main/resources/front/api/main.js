/**
 * 主页面API（移动端）
 * 对应后端多个Controller
 */

/**
 * 获取所有菜品分类
 * @returns {Promise} 分类列表
 */
function categoryListApi() {
  return $axios({
    url: '/category/list',
    method: 'get'
  })
}

/**
 * 获取指定分类下的菜品列表
 * @param {Object} data - 查询参数
 * @param {number} data.categoryId - 分类ID
 * @returns {Promise} 菜品列表
 */
function dishListApi(data) {
  return $axios({
    url: '/dish/list',
    method: 'get',
    params: { ...data }
  })
}

/**
 * 获取指定分类下的套餐列表
 * @param {Object} data - 查询参数
 * @param {number} data.categoryId - 分类ID
 * @returns {Promise} 套餐列表
 */
function setmealListApi(data) {
  return $axios({
    url: '/setmeal/list',
    method: 'get',
    params: { ...data }
  })
}

/**
 * 获取当前用户购物车列表
 * @returns {Promise} 购物车列表
 */
function cartListApi() {
  return $axios({
    url: '/shoppingCart/list',
    method: 'get'
  })
}

/**
 * 购物车添加商品
 * @param {Object} data - 商品信息
 * @returns {Promise} 操作结果
 */
function addCartApi(data) {
  return $axios({
    url: '/shoppingCart/add',
    method: 'post',
    data
  })
}

/**
 * 购物车减少商品数量
 * @param {Object} data - 商品信息
 * @returns {Promise} 操作结果
 */
function subCartApi(data) {
  return $axios({
    url: '/shoppingCart/sub',
    method: 'post',
    data
  })
}

/**
 * 清空购物车
 * @returns {Promise} 操作结果
 */
function clearCartApi() {
  return $axios({
    url: '/shoppingCart/clean',
    method: 'delete'
  })
}


