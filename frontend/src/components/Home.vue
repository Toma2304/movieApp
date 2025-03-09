<template>
  <div class="container home">
    <h1 id="toTop">Home</h1>
    <ProgressSpinner v-if="!news" class="charging"/>
    <div v-else>
      <div class="news">
        <h2>Latest <span>(from "<a href="https://www.nytimes.com/section/movies" target="_blank">The New York Times</a>")</span></h2>
        <div class="article-list">
          <article v-for="item in news"  @click="copyLink(item)">
            <div class="image" :style="{'background-image': 'url(' + item.image + ')'}"></div>
            <div class="text-box">
              <h3 v-html="item.headline"></h3>
              <p v-html="item.description"></p>
              <Tag :value="item.source"></Tag>
            </div>
          </article>
        </div>
      </div>

      <div class="pagination">
        <a href="#toTop"><Button icon="pi pi-angle-double-left" variant="outlined" size="large" @click="loadPage(false)" :disabled="page <= 1" /></a>
        <a href="#toTop"><Button icon="pi pi-angle-double-right" variant="outlined" size="large" @click="loadPage(true)" /></a>
      </div>
      <Toast />
    </div>
  </div>
</template>

<script setup>
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import Button from 'primevue/button';
import {onMounted, ref} from "vue";
import axios from "axios";
import ProgressSpinner from "primevue/progressspinner";
import {useToast} from "primevue";

const news = ref();
const toast = useToast();
const page = ref(1);

onMounted(() => {
  fetchNews(page.value);
});

function loadPage(value) {
  if(!value) {
    page.value--;
  } else {
    page.value++;
  }

  fetchNews(page.value);
}

async function fetchNews(pageNumber) {
  news.value = null;
  try {
    const response = await axios.get(`/news?page=${pageNumber}`);
    news.value = response.data;
  } catch (error) {
    console.error("Error fetching news:", error);
  }
}

async function copyLink(item) {
  try {
    await navigator.clipboard.writeText('https://www.nytimes.com/' + item.link);
    toast.add({ severity: 'success', summary: 'Success', detail: 'The link is copied!', life: 3000 });
  } catch (err) {
    console.error('Failed to copy text', err);
  }
}
</script>