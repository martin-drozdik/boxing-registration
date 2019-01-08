function make_empty_boxer(club) {

    console.log(typeof club);
    return { year: '', category: '', n_fights: '', name: '', club: club };
}

new Vue({
    el: '#boxerlist',
    data: 
    {
        members: [make_empty_boxer(this.club)],
        club: ""
    },

    watch: 
    {
        club: function(newClub) 
        {
            let url = `/club/${this.club}`;
            $.get(url, (data) => { 
                if (data.length == 0)
                {
                    console.log(typeof newClub);
                    this.members = [make_empty_boxer(newClub)];
                }
                else
                    this.members = data;
            });
        }
    },
   
    methods: 
    {
        send_to_server: function() 
        {
            console.log(typeof this.members[0].club)
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

        kick_boxer: function(event, index) 
        {
            this.members.splice(index, 1);
            if (this.members.length == 0)
                this.members = [make_empty_boxer(this.club)];
        }
    }
});