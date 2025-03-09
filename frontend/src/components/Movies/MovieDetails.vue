<template>
  <div class="container">
    <ProgressSpinner v-if="!details" class="charging"/>
    <div v-else class="movie-details">
      <div>
        <h1 v-html="details.title"></h1>
        <span class="subtitle" v-html="details.tagline"></span>
        <div class="tags">
          <Tag v-for="item in details.genres" :value="item.name" severity="info"></Tag>
        </div>
      </div>
      <div class="movie-details__wrapper">
        <img :alt="details.title" :src="'https://image.tmdb.org/t/p/original/'+ details.poster_path">
        <div>
          <h3>Release: <span v-html="details.release_date"></span></h3>
          <h3>Runtime:
            <Tag :value="details.runtime + ' min'" severity="success"></Tag>
          </h3>
          <div v-if="details.budget !== 0">
            <h3>Budget:
              <Tag :value="convertPrice(details.budget)" severity="success"></Tag>
            </h3>
          </div>
          <div v-if="details.revenue !== 0">
            <h3>Revenue:
              <Tag :value="convertPrice(details.revenue)" severity="success"></Tag>
            </h3>
          </div>
          <div>
            <h3>Description</h3>
            <p v-html="details.overview"></p>
          </div>
          <div>
            <h3>Rating</h3>
            <div class="rating">
              <Rating v-model="details.vote_average" :stars="10" readonly/>
              <span class="text">( {{ details.vote_count }} votes )</span>
            </div>
          </div>
          <div style="margin-top: 30px">
            <h3>Production</h3>
            <div class="production">
              <img v-for="item in details.production_companies"
                   :alt="item.name" :src="'https://image.tmdb.org/t/p/original/'+ item.logo_path">
            </div>
          </div>
          <div>
            <h3>Official website: <span><a :href="details.homepage" target="_blank" v-html="details.title"></a></span>
            </h3>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Rating from 'primevue/rating';
import ProgressSpinner from 'primevue/progressspinner';
import {Tag} from 'primevue';
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRoute} from "vue-router";

const details = ref();
const route = useRoute();

onMounted(async () => {
  try {
    const movieDetails = await axios.get('/api/popular-movies/' + route.params.movie);
    details.value = movieDetails.data;
    console.log('Movie Details:', details.value);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});

function convertPrice(price) {
  return _.toNumber(price).toLocaleString('en-US').replace(/,/g, ' ') + ' $';
}

</script>
