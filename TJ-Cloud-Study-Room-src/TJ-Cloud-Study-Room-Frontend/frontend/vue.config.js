// 跨域配置
module.exports = {
  devServer: {
    // port 选填
    port: 80,
    proxy: {                 //设置代理，必须填
      '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
        target: 'http://localhost:9090',     //代理的目标地址
        changeOrigin: true,              //是否设置同源，输入是的
        pathRewrite: {                   //路径重写
          '^/api': ''                     //选择忽略拦截器里面的内容
        }
      },
      '/messageApi':{
        target: 'https://uni.apistd.com',
        changeOrigin: true,
        pathRewrite: {
          '^/messageApi': ''
        }
      },
      '/wsApi':{
        target: 'ws://localhost:9090',
        changeOrigin: true,
        pathRewrite: {
          '^/wsApi': ''
        }
      }
    }
  }
}
