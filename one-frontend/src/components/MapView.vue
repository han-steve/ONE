<template>
    <div class="container mt-3 mt-sm-5" id="app">
        <div class="row">
            <div class="col-md-9">
                <div class="map" id="map"></div>
            </div>
            <div class="col-md-3">
                <div
                        class="form-check"
                        v-for="layer of getStoredLayers"
                        :key="layer.id"
                >
                    <label class="form-check-label">
                        <input
                                class="form-check-input"
                                type="checkbox"
                                v-model="layer.active"
                                @change="layerChanged(layer.id, layer.active)"
                        />
                        {{ layer.name }}
                    </label>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        name: "MapView",
        props: ['map'],
        methods: {
            layerChanged(layerId, active) {
                const layer = this.$store.state.layers.find(layer => layer.id === layerId);

                layer.features.forEach(function (feature) {
                    if (active) {
                        feature.leafletObject.addTo(this.map);
                    } else {
                        feature.leafletObject.removeFrom(this.map);
                    }
                }, this);
            }
        },
        computed: {
            getStoredLayers: function() {
                return this.$store.state.layers;
            }
        }
    }
</script>

<style scoped>
#map {
    height: 40em;
    width: 70em;
}
</style>
