<template>
  <main class="first-background">
    <h1>Matches</h1>
    <div class="second-background">
      <map-view :map="map"></map-view>
    </div>
  </main>
</template>

<script>
import MapView from "@/components/MapView.vue";
import TransactionTable from "@/components/TransactionTable.vue";

export default {
  components: {
    MapView,
    TransactionTable
  },
  data() {
    return {
      map: null,
      tileLayer: null
    }
  },
  mounted() {
    this.initMap();
    this.initLayers();
  },
  methods: {
    initLayers() {
      this.$store.state.layers.forEach((layer) => {
        const markerFeatures = layer.features.filter(feature => feature.type === 'marker');
        const polygonFeatures = layer.features.filter(feature => feature.type === 'polygon');

        markerFeatures.forEach((feature) => {
          feature.leafletObject = L.marker(feature.coords)
                  .bindPopup(feature.name);
        });

        polygonFeatures.forEach((feature) => {
          feature.leafletObject = L.polygon(feature.coords)
                  .bindPopup(feature.name);
        });
      });
    },
    initMap() {
      this.map = L.map('map').setView([38.63, -90.23], 12);
      this.tileLayer = L.tileLayer(
              'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
              {
                maxZoom: 18,
                // attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
                id: 'mapbox.streets',
                accessToken: 'pk.eyJ1IjoidGltb3RoeXRxaW4iLCJhIjoiY2sxeWV3ZjF4MG1uczNtczV1YXkwYmlnayJ9.yuo-wMu_zym_hwUuz3MHRw'
              }
      );

      this.tileLayer.addTo(this.map);
    }
  }
};
</script>

<style scoped>
</style>
