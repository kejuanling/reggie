/**
 * 用户登录API（移动端）
 * 对应后端 UserController
 */

/**
 * 用户登录（验证码登录）
 * @param {Object} data - 登录信息
 * @param {string} data.phone - 手机号
 * @param {string} data.code - 验证码
 * @returns {Promise} 登录结果
 */
function loginApi(data) {
  return $axios({
    url: '/user/login',
    method: 'post',
    data
  })
}

/**
 * 用户退出登录
 * @returns {Promise} 退出结果
 */
function loginoutApi() {
  return $axios({
    url: '/user/loginout',
    method: 'post'
  })
}

  