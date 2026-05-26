<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="促销活动" name="1">
        <material-list :subject="1" :materialType="1" />
      </el-tab-pane>
      <el-tab-pane label="热门主题" name="2">
        <material-list :subject="2" :materialType="1" />
      </el-tab-pane>
      <el-tab-pane label="精选榜单" name="3">
        <material-list :subject="3" :materialType="1" />
      </el-tab-pane>
      <el-tab-pane label="行业频道" name="4">
        <material-list :subject="4" :materialType="1" />
      </el-tab-pane>
      <el-tab-pane label="其他" name="5">
        <material-list :subject="5" :materialType="1" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import MaterialList from './materialList.vue';

export default {
  name: "TbkMaterial",
  components: {
    MaterialList
  },
  data() {
    return {
      activeTab: "1"
    };
  },
  methods: {
    handleTabClick(tab) {
      console.log('切换标签页:', tab.name);
    }
  }
};
</script>
