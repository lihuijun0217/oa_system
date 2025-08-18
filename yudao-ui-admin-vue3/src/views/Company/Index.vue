<template>
    <div class="company-homepage">
      <!-- 顶部导航 -->
      <!-- <header class="header">
        <div class="container">
          <div class="logo">
            <h1>智慧物业</h1>
          </div>
          <nav class="nav">
            <a href="#home" class="nav-item">首页</a>
            <a href="#services" class="nav-item">服务项目</a>
            <a href="#about" class="nav-item">关于我们</a>
            <a href="#contact" class="nav-item">联系我们</a>
          </nav>
        </div>
      </header> -->
  
      <!-- 主横幅 -->
      <section id="home" class="hero">
        <div class="container">
          <div class="hero-content">
            <h2>专业的物业管理服务</h2>
            <p>为您提供全方位的物业管理解决方案，让您的物业价值最大化</p>
            <button class="cta-button">了解更多</button>
          </div>
        </div>
      </section>
  
      <!-- 服务项目 -->
      <section id="services" class="services">
        <div class="container">
          <h2 class="section-title">我们的服务</h2>
          <div class="services-grid">
            <div class="service-card">
              <div class="service-icon">🏢</div>
              <h3>住宅物业管理</h3>
              <p>为住宅小区提供全面的物业管理服务，包括安保、清洁、维修等</p>
            </div>
            <div class="service-card">
              <div class="service-icon">🏭</div>
              <h3>商业物业管理</h3>
              <p>为商业地产提供专业的物业管理，确保商业环境的良好运营</p>
            </div>
            <div class="service-card">
              <div class="service-icon">🔧</div>
              <h3>设施维护</h3>
              <p>提供设备维护、保养、维修等专业技术服务</p>
            </div>
            <div class="service-card">
              <div class="service-icon">💰</div>
              <h3>财务管理</h3>
              <p>专业的财务管理和费用收取服务，确保资金安全</p>
            </div>
          </div>
        </div>
      </section>
  
      <!-- 关于我们 -->
      <section id="about" class="about">
        <div class="container">
          <div class="about-content">
            <div class="about-text">
              <h2>关于智慧物业</h2>
              <p>智慧物业成立于2010年，是一家专业的物业管理服务公司。我们致力于为客户提供高品质、高效率的物业管理服务。</p>
              <p>公司拥有专业的团队和丰富的管理经验，服务覆盖住宅、商业、办公等多种物业类型。</p>
              <div class="stats">
                <div class="stat-item">
                  <div class="stat-number">500+</div>
                  <div class="stat-label">服务项目</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">50,000+</div>
                  <div class="stat-label">服务业主</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">13</div>
                  <div class="stat-label">年经验</div>
                </div>
              </div>
            </div>
            <div class="about-image">
              <div class="image-placeholder">公司图片</div>
            </div>
          </div>
        </div>
      </section>
  
      <!-- 联系我们 -->
      <section id="contact" class="contact">
        <div class="container">
          <h2 class="section-title">联系我们</h2>
          <div class="contact-content">
            <div class="contact-info">
              <div class="contact-item">
                <div class="contact-icon">📍</div>
                <div>
                  <h4>地址</h4>
                  <p>某某大厦16层</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">📞</div>
                <div>
                  <h4>电话</h4>
                  <p>400-123-4567</p>
                </div>
              </div>
              <div class="contact-item">
                <div class="contact-icon">✉️</div>
                <div>
                  <h4>邮箱</h4>
                  <p>info@smartproperty.com</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  
      <!-- 页脚 -->
      <footer class="footer">
        <div class="container">
          <p>&copy; 2024 智慧物业. 版权所有.</p>
        </div>
      </footer>
    </div>
  </template>
  
  <script setup lang="ts">
  defineOptions({ name: 'CompanyHomepage' })
  </script>
  
  <style scoped>
  .company-homepage {
    font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  }
  
  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }
  
  /* 顶部导航 */
  .header {
    background: #fff;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
  }
  
  .header .container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 70px;
  }
  
  .logo h1 {
    color: #1890ff;
    margin: 0;
    font-size: 24px;
    font-weight: bold;
  }
  
  .nav {
    display: flex;
    gap: 30px;
  }
  
  .nav-item {
    text-decoration: none;
    color: #333;
    font-weight: 500;
    transition: color 0.3s;
  }
  
  .nav-item:hover {
    color: #1890ff;
  }
  
  /* 主横幅 */
  .hero {
    background: linear-gradient(135deg, #1890ff 0%, #52c41a 100%);
    color: white;
    padding: 120px 0 80px;
    text-align: center;
  }
  
  .hero-content h2 {
    font-size: 48px;
    margin-bottom: 20px;
    font-weight: bold;
  }
  
  .hero-content p {
    font-size: 20px;
    margin-bottom: 30px;
    opacity: 0.9;
  }
  
  .cta-button {
    background: white;
    color: #1890ff;
    border: none;
    padding: 15px 30px;
    font-size: 18px;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
    transition: transform 0.3s;
  }
  
  .cta-button:hover {
    transform: translateY(-2px);
  }
  
  /* 服务项目 */
  .services {
    padding: 80px 0;
    background: #f8f9fa;
  }
  
  .section-title {
    text-align: center;
    font-size: 36px;
    margin-bottom: 50px;
    color: #333;
  }
  
  .services-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 30px;
  }
  
  .service-card {
    background: white;
    padding: 30px;
    border-radius: 10px;
    text-align: center;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    transition: transform 0.3s;
  }
  
  .service-card:hover {
    transform: translateY(-5px);
  }
  
  .service-icon {
    font-size: 48px;
    margin-bottom: 20px;
  }
  
  .service-card h3 {
    font-size: 20px;
    margin-bottom: 15px;
    color: #333;
  }
  
  .service-card p {
    color: #666;
    line-height: 1.6;
  }
  
  /* 关于我们 */
  .about {
    padding: 80px 0;
  }
  
  .about-content {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 50px;
    align-items: center;
  }
  
  .about-text h2 {
    font-size: 36px;
    margin-bottom: 20px;
    color: #333;
  }
  
  .about-text p {
    font-size: 16px;
    line-height: 1.8;
    color: #666;
    margin-bottom: 20px;
  }
  
  .stats {
    display: flex;
    gap: 30px;
    margin-top: 30px;
  }
  
  .stat-item {
    text-align: center;
  }
  
  .stat-number {
    font-size: 32px;
    font-weight: bold;
    color: #1890ff;
  }
  
  .stat-label {
    font-size: 14px;
    color: #666;
    margin-top: 5px;
  }
  
  .about-image {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .image-placeholder {
    width: 300px;
    height: 200px;
    background: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 10px;
    color: #999;
    font-size: 18px;
  }
  
  /* 联系我们 */
  .contact {
    padding: 80px 0;
    background: #f8f9fa;
  }
  
  .contact-content {
    display: flex;
    justify-content: center;
  }
  
  .contact-info {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 30px;
    max-width: 800px;
  }
  
  .contact-item {
    display: flex;
    align-items: center;
    gap: 15px;
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  }
  
  .contact-icon {
    font-size: 24px;
  }
  
  .contact-item h4 {
    margin: 0 0 5px 0;
    color: #333;
  }
  
  .contact-item p {
    margin: 0;
    color: #666;
  }
  
  /* 页脚 */
  .footer {
    background: #333;
    color: white;
    text-align: center;
    padding: 20px 0;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .nav {
      display: none;
    }
    
    .hero-content h2 {
      font-size: 32px;
    }
    
    .about-content {
      grid-template-columns: 1fr;
    }
    
    .stats {
      flex-direction: column;
      gap: 20px;
    }
    
    .contact-info {
      grid-template-columns: 1fr;
    }
  }
  </style>