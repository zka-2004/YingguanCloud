<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-card">
      <div class="welcome-header">
        <h1 class="welcome-title">欢迎回来，<span class="username">{{ name || '超级管理员' }}</span></h1>
        <p class="welcome-desc">今天是 {{ today }}，数据概览已更新</p>
      </div>
      <div class="welcome-decoration"></div>
    </div>

    <!-- 图表网格容器 -->
    <div class="charts-grid">
      <!-- 柱状图 - 销售数据 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>商品销量分析</h3>
          <p>各类商品月度销量对比</p>
        </div>
        <div class="chart-container" id="barChart"></div>
      </div>

      <!-- 折线图 - 趋势分析 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>销售趋势追踪</h3>
          <p>近6个月销售额变化</p>
        </div>
        <div class="chart-container" id="lineChart"></div>
      </div>

      <!-- 饼图 - 占比分析 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>品类占比分布</h3>
          <p>各品类销售额占比</p>
        </div>
        <div class="chart-container" id="pieChart"></div>
      </div>

      <!-- 雷达图 - 多维度分析 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>产品维度评分</h3>
          <p>各品类在5个维度的表现</p>
        </div>
        <div class="chart-container" id="radarChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import * as echarts from 'echarts';

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters(['name']),
    today() {
      const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
      return new Date().toLocaleDateString('zh-CN', options);
    }
  },
  data() {
    return {
      charts: {} // 存储所有图表实例
    };
  },
  methods: {
    // 初始化所有图表
    initCharts() {
      this.initBarChart();
      this.initLineChart();
      this.initPieChart();
      this.initRadarChart();
    },

    // 柱状图初始化
    initBarChart() {
      const chart = echarts.init(document.getElementById('barChart'));
      this.charts.bar = chart;

      chart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          backgroundColor: 'rgba(255,255,255,0.9)',
          borderColor: '#eee',
          borderRadius: 8,
          padding: 10,
          textStyle: { color: '#333' }
        },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子'],
          axisLine: { lineStyle: { color: '#eee' } },
          axisTick: { show: false }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { lineStyle: { color: '#f5f5f5' } }
        },
        series: [{
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20],
          barWidth: '60%',
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#409eff' },
              { offset: 1, color: '#69b1ff' }
            ])
          },
          animationDuration: 1500,
          animationEasing: 'elasticOut'
        }]
      });
    },

    // 折线图初始化
    initLineChart() {
      const chart = echarts.init(document.getElementById('lineChart'));
      this.charts.line = chart;

      chart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255,255,255,0.9)',
          borderColor: '#eee',
          borderRadius: 8
        },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月'],
          axisLine: { lineStyle: { color: '#eee' } },
          axisTick: { show: false }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { lineStyle: { color: '#f5f5f5' } }
        },
        series: [{
          name: '销售额',
          type: 'line',
          data: [1200, 1900, 3000, 2400, 2800, 4000],
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          emphasis: { symbolSize: 10 },
          lineStyle: { width: 3, color: '#722ed1' },
          itemStyle: {
            color: '#722ed1',
            borderColor: '#fff',
            borderWidth: 2
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(114, 46, 209, 0.3)' },
              { offset: 1, color: 'rgba(114, 46, 209, 0)' }
            ])
          },
          animationDuration: 2000
        }]
      });
    },

    // 饼图初始化
    initPieChart() {
      const chart = echarts.init(document.getElementById('pieChart'));
      this.charts.pie = chart;

      chart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(255,255,255,0.9)',
          borderColor: '#eee',
          borderRadius: 8
        },
        legend: {
          orient: 'vertical',
          left: 10,
          textStyle: { color: '#666' }
        },
        series: [{
          name: '品类分布',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: { show: false, position: 'center' },
          emphasis: {
            label: { show: true, fontSize: 16, fontWeight: 'bold' }
          },
          labelLine: { show: false },
          data: [
            { value: 335, name: '衬衫' },
            { value: 310, name: '羊毛衫' },
            { value: 234, name: '雪纺衫' },
            { value: 135, name: '裤子' },
            { value: 1548, name: '高跟鞋' }
          ],
          color: ['#ff7d00', '#0fc6c2', '#722ed1', '#f53f3f', '#00b42a'],
          animationDuration: 1500,
          animationEasingUpdate: 'quinticOut'
        }]
      });
    },

    // 雷达图初始化
    initRadarChart() {
      const chart = echarts.init(document.getElementById('radarChart'));
      this.charts.radar = chart;

      chart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255,255,255,0.9)',
          borderColor: '#eee',
          borderRadius: 8
        },
        radar: {
          indicator: [
            { name: '质量', max: 100 },
            { name: '价格', max: 100 },
            { name: '销量', max: 100 },
            { name: '口碑', max: 100 },
            { name: '热度', max: 100 }
          ],
          splitArea: { areaStyle: { color: ['rgba(255,255,255,0.2)'] } },
          axisLine: { lineStyle: { color: '#ddd' } },
          splitLine: { lineStyle: { color: '#ddd' } }
        },
        series: [{
          name: '品类对比',
          type: 'radar',
          symbol: 'circle',
          symbolSize: 6,
          lineStyle: { width: 2 },
          emphasis: { areaStyle: { color: 'rgba(247, 186, 41, 0.3)' } },
          areaStyle: { opacity: 0.2 },
          data: [
            {
              value: [80, 60, 90, 70, 85],
              name: '衬衫',
              itemStyle: { color: '#f7ba2a' },
              lineStyle: { color: '#f7ba2a' }
            },
            {
              value: [90, 75, 65, 80, 70],
              name: '羊毛衫',
              itemStyle: { color: '#00b42a' },
              lineStyle: { color: '#00b42a' }
            },
            {
              value: [70, 85, 75, 65, 90],
              name: '高跟鞋',
              itemStyle: { color: '#f53f3f' },
              lineStyle: { color: '#f53f3f' }
            }
          ],
          animationDuration: 2000
        }]
      });
    },

    // 响应窗口大小变化
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        chart && chart.resize();
      });
    }
  },
  mounted() {
    // 初始化图表
    this.initCharts();
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    // 销毁所有图表实例
    Object.values(this.charts).forEach(chart => {
      chart && chart.dispose();
    });
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize);
  }
};
</script>

<style lang="scss" scoped>
// 全局样式变量
$primary-color: #409eff;
$light-primary: #e8f3ff;
$text-primary: #303133;
$text-secondary: #606266;
$bg-color: #f5f7fa;
$card-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
$transition-base: all 0.3s ease;

.dashboard-container {
  min-height: 100vh;
  padding: 2rem;
  background-color: $bg-color;
  box-sizing: border-box;

  @media (max-width: 768px) {
    padding: 1rem;
  }
}

// 欢迎卡片样式
.welcome-card {
  position: relative;
  background-color: #fff;
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: $card-shadow;
  overflow: hidden;
  transition: $transition-base;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  }

  .welcome-header {
    position: relative;
    z-index: 2;
  }

  .welcome-title {
    margin: 0 0 0.5rem 0;
    color: $text-primary;
    font-size: 1.8rem;
    font-weight: 600;
  }

  .username {
    color: $primary-color;
    position: relative;
    display: inline-block;

    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: -2px;
      width: 100%;
      height: 3px;
      background-color: $light-primary;
      border-radius: 3px;
    }
  }

  .welcome-desc {
    margin: 0;
    color: $text-secondary;
    font-size: 1rem;
    line-height: 1.6;
  }

  .welcome-decoration {
    position: absolute;
    right: -50px;
    top: -50px;
    width: 200px;
    height: 200px;
    background-color: $light-primary;
    border-radius: 50%;
    opacity: 0.5;
    z-index: 1;
  }
}

// 图表网格布局
.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(500px, 1fr));
  gap: 2rem;

  @media (max-width: 1100px) {
    grid-template-columns: 1fr;
  }

  @media (max-width: 768px) {
    gap: 1rem;
  }
}

// 图表卡片样式
.chart-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: $card-shadow;
  transition: $transition-base;
  display: flex;
  flex-direction: column;
  height: 100%;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  }

  .chart-header {
    margin-bottom: 1rem;

    h3 {
      margin: 0 0 0.3rem 0;
      color: $text-primary;
      font-size: 1.1rem;
      font-weight: 600;
    }

    p {
      margin: 0;
      color: $text-secondary;
      font-size: 0.85rem;
    }
  }

  .chart-container {
    width: 100%;
    flex: 1;
    min-height: 300px;
  }
}

// 入场动画
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-card, .chart-card {
  animation: fadeIn 0.5s ease forwards;
  opacity: 0;
}

.welcome-card { animation-delay: 0.1s; }
.chart-card:nth-child(1) { animation-delay: 0.3s; }
.chart-card:nth-child(2) { animation-delay: 0.5s; }
.chart-card:nth-child(3) { animation-delay: 0.7s; }
.chart-card:nth-child(4) { animation-delay: 0.9s; }
</style>
