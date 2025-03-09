<template>
  <div class="container">
    <h1 id="toTop">Popular Movies</h1>
    <ProgressSpinner v-if="!popularMovies" class="charging"/>
   <div v-else>
     <DataTable :value="popularMovies" stripedRows>
       <Column field="id" header="ID" sortable></Column>
       <Column field="original_language" header="Lang" sortable style="width: 3%"></Column>
       <Column field="release_date" header="Release" sortable></Column>
       <Column field="title" header="Title" sortable></Column>
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
       <Column field="vote_average" header="Reviews" sortable>
         <template #body="slotProps">
           <span style="color: gray; font-size: 13px" v-html="slotProps.data.vote_average + '/10'"></span>
         </template>
       </Column>
       <Column header="View">
         <template #body="slotProps">
           <Button icon="pi pi-forward" rounded @click="openMovie(slotProps.data)"/>
           <Button icon="pi pi-heart" outlined severity="danger" rounded @click="saveToFavorite(slotProps.data)" v-if="isAuthenticated && !isFavorite(slotProps.data.id)" style="margin-top: 8px" />
           <Button icon="pi pi-heart" severity="danger" rounded v-else-if="isAuthenticated" @click="deleteFromFavorite(slotProps.data)" style="margin-top: 8px" />
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

const popularMovies = ref();
const favoriteMovies = ref();
const genres = ref();
const router = useRouter();
const loading = ref(false);
const page = ref(1);

const isFavorite = (id) => {
  return _.find(favoriteMovies.value, { item_id: id });
};

const isAuthenticated = computed(() => {
  return !_.isNil(localStorage.getItem('isLoggedIn'));
})

onMounted(async () => {
  try {
    const [moviesResponse, genresResponse] = await Promise.all([
      axios.get('/api/popular-movies?page=1'),
      axios.get('/api/movies/genres')
    ]);
    popularMovies.value = moviesResponse.data.popularMovies.results;
    favoriteMovies.value = moviesResponse.data.userFavorites;
    genres.value = genresResponse.data.genres;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});

async function fetchMovies(pageNumber) {
  popularMovies.value = null;
  try {
    const response = await axios.get(`/api/popular-movies?page=${pageNumber}`);
    popularMovies.value = response.data.popularMovies.results;
    favoriteMovies.value = response.data.userFavorites;
  } catch (error) {
    console.error("Error fetching popular movies:", error);
  }
}

function loadPage(value) {
  if(!value) {
    page.value--;
  } else {
    page.value++;
  }

  fetchMovies(page.value);
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

function openMovie(item) {
  router.push({
    name: 'movieDetails',
    params: {movie: item.id}
  });
}

async function saveToFavorite(item) {
  const data = {
    itemId: item.id,
    title: item.title,
    poster: item.poster_path,
    type: 'movies'
  }

  try {
    const response = await axios.post('/favorites', data);
    location.reload();
  } catch (error) {
    console.error("Error fetching movie genres:", error);
  }
}

async function deleteFromFavorite(item) {
  const favorite_id = _.find(favoriteMovies.value, {'item_id': item.id}).id;
  try {
    const response = await axios.delete('/favorites/' + favorite_id);
    location.reload();
  } catch (error) {
    console.error("Error fetching movie genres:", error);
  }
}

</script>