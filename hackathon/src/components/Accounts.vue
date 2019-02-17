<template>
    <div>
        <select v-bind="account">

        </select>
    </div>
</template>

<script>
    import navTpl from "@/components/NavTpl"

    export default {
        name: 'accounts',
        components: {navTpl},
        accounts: [],
        data() {
            return {
                account: '';
            }
        },
        methods: {
            populate() {

            }
        }
    }
</script>
