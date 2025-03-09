<template>
  <div class="container">
    <h1 id="toTop">Popular TV Shows</h1>
    <ProgressSpinner v-if="!popularSeries" class="charging"/>
   <div v-else>
     <DataTable :value="popularSeries" stripedRows>
       <Column field="id" header="ID" sortable></Column>
       <Column field="original_language" header="Lang" sortable style="width: 3%"></Column>
       <Column field="original_name" header="Title" sortable></Column>
       <Column header="Overview">
         <template #body="slotProps">
           <p v-html="getOverview(slotProps.data.overview)"></p>
         </template>
       </Column>
       <Column header="Genres">
         <template #body="slotProps">
           <Tag v-for="item in slotProps.data.genre_ids" :value="getGenre(item)" severity="success"
                style="margin: 5px; white-space: nowrap"></Tag>
         </template>
       </Column>
       <Column header="Poster">
         <template #body="slotProps">
           <img :src="'https://image.tmdb.org/t/p/original/'+ slotProps.data.poster_path" alt="" style="max-width: 100%">
         </template>
       </Column>
       <Column field="vote_average" header="Reviews" sortable>z
         <template #body="slotProps">
           <span style="color: gray; font-size: 13px" v-html="slotProps.data.vote_average + '/10'"></span>
         </template>
       </Column>
       <Column header="View">
         <template #body="slotProps">
           <Button icon="pi pi-forward" rounded @click="openSeries(slotProps.data)"/>
         </template>
       </Column>
     </DataTable>
     <div class="pagination">
       <a href="#toTop"><Button icon="pi pi-angle-double-left" variant="outlined" size="large" @click="loadPage(false)" :disabled="page <= 1" /></a>
       <a href="#toTop"><Button icon="pi pi-angle-double-right" variant="outlined" size="large" @click="loadPage(true)" /></a>
     </div>
   </div>
  </div>
</template>

<script setup>
import Tag from 'primevue/tag';
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import {Button} from "primevue";
import {useRouter} from "vue-router";
import ProgressSpinner from "primevue/progressspinner";

const popularSeries = ref();
const genres = ref();
const router = useRouter();
const page = ref(1);

const isAuthenticated = computed(() => {
  return !_.isNil(localStorage.getItem('isLoggedIn'));
})

onMounted(async () => {
  try {
    const [seriesResponse, genresResponse] = await Promise.all([
      axios.get('/api/popular-series?page=1'),
      axios.get('/api/series/genres')
    ]);
    popularSeries.value = seriesResponse.data.results;
    genres.value = genresResponse.data.genres;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});


async function fetchSeries(pageNumber) {
  popularSeries.value = null;
  try {
    const response = await axios.get(`/api/popular-series?page=${pageNumber}`);
    popularSeries.value = response.data.results;
  } catch (error) {
    console.error("Error fetching popular series:", error);
  }
}

function loadPage(value) {
  if(!value) {
    page.value--;
  } else {
    page.value++;
  }

  fetchSeries(page.value);
}

function getGenre(item) {
  return _.find(genres.value, {'id': item}).name;
}

function getOverview(item) {
  return _.truncate(item, {
    length: 200,
    omission: '...'
  });
}

function openSeries(item) {
  router.push({
    name: 'seriesDetails',
    params: {series: item.id}
  });
}

</script>