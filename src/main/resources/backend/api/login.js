/**
 * 登录管理API
 * 对应后端 EmployeeController
 */

/**
 * 员工登录
 * @param {Object} data - 登录信息
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise} 登录结果
 */
function loginApi(data) {
  return $axios({
    url: '/employee/login',
    method: 'post',
    data
  })
}

/**
 * 员工退出登录
 * @returns {Promise} 退出结果
 */
function logoutApi() {
  return $axios({
    url: '/employee/logout',
    method: 'post'
  })
}
