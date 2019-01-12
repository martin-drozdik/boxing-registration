function make_empty_boxer(club) 
{
    return { year_category: '', weight_category: '', n_fights: '', name: '', club: club };
}

new Vue({

    el: '#boxerlist',

    data: 
    {
        members: [make_empty_boxer(this.club)],
        club: "",
        year_to_categories: {},
        all_year_categories: [],
        all_clubs: []
    },

    created: function () 
    {
        $.get("categories", ( categories ) => 
        {
            for (let category of categories)
            {
                this.year_to_categories[category.year] = category.categories;
            }
            this.all_year_categories = Object.keys(this.year_to_categories);
        });

        $.get("clubs", ( clubs ) =>
        {
            this.all_clubs = clubs;
        });
    },

    watch: 
    {
        club: function(newClub) 
        {
            let url = `/club/${newClub}`;
            $.get(url, (data) => { 
                if (data.length == 0)
                {
                    this.members = [make_empty_boxer(newClub)];
                }
                else
                {
                    this.members = data;
                }
            });
        }
    },
   
    methods: 
    {
        send_to_server: function() 
        {
            $.post({ 
                url: "/members/update",
                data : JSON.stringify({ club: this.club, members: this.members }),
                contentType : 'application/json'
            })
        },

        add_boxer: function(event, index) 
        {
            this.members.push(make_empty_boxer(this.club));
        },

        remove_boxer: function(event, index) 
        {
            this.members.splice(index, 1);
            if (this.members.length == 0)
            {
                this.members = [make_empty_boxer(this.club)];
            }
        },

        get_possible_weight_categories: function(year_category)
        {
            if (year_category == '')
            {
                return [];
            }
            return this.year_to_categories[year_category];
        },

        get_possible_year_categories: function()
        {
            return Object.keys(window.year_to_categories);
        }
    }
});