<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="大额店铺券" name="大额店铺券">
        <promotion-list promotionId="37116" />
      </el-tab-pane>
      <el-tab-pane label="天猫店铺券" name="天猫店铺券">
        <promotion-list promotionId="62191" />
      </el-tab-pane>
      <el-tab-pane label="券券补" name="券券补">
        <promotion-list promotionId="61809" />
      </el-tab-pane>
      <el-tab-pane label="有价券" name="有价券">
        <promotion-list promotionId="37104" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import PromotionList from './promotionList.vue';

export default {
  name: "TbkPromotion",
  components: {
    PromotionList
  },
  data() {
    return {
      activeTab: "all"
    };
  },
  methods: {
    handleTabClick(tab) {
      console.log('切换标签页:', tab.name);
    }
  }
};
</script>
