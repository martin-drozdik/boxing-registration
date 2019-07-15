function make_empty_boxer(club) 
{
    return { year_category: '', weight_category: '', n_fights: '', name: '', club: club };
}

let logindetails = { username: "", password: "", club: "" };

let beforeSend = function (xhr) 
{
    xhr.setRequestHeader ("Authorization", "Basic " + btoa(logindetails.username + ":" + logindetails.password));
}

new Vue({

    el: '#coach-registration',

    data: {
        coach: { name: "", email: "", password: "", club: "" },
        all_clubs: []
    },

    created: function () 
    {
        $.get("/api/clubs", ( clubs ) =>
        {
            this.all_clubs = clubs;
        });
    },

    methods: 
    {
        send_to_server: function()
        {
            $.post({ 
                url: "/api/register",
                data : JSON.stringify(this.coach),
                contentType : 'application/json',
                success: function()
                {
                    $('.page').addClass('hidden');
                    $(".page-login").removeClass('hidden');
                }
            })
        }
    }
});


var loginVue = new Vue({

    el: '#login',

    data:
    {
        logindetails: { username: "", password: "", club: "" }
    },

    methods: 
    {
        send_to_server: function()
        {
            let self = this;
            $.get({ 
                url: "/api/login",
                contentType : 'application/json',
                beforeSend: beforeSend,
                success: function(data)
                {                   
                    logindetails.club = self.logindetails.club;
                    logindetails.username = self.logindetails.username;
                    logindetails.password = self.logindetails.password;

                    $('.page').addClass('hidden');
                    $(".page-registration").removeClass('hidden');

                    $("#registration-nav").removeClass('hidden');
                    $("#all-nav").removeClass('hidden');
                    

                    $.get(
                        {
                            url: "/api/members", 
                            beforeSend: beforeSend,
                            success: ( members ) => 
                            {
                                allVue.members = members;
                            }
                        });

                    console.log(`Club changed to ${logindetails.club}`)
                }
            })
        }
    }
});


new Vue({

    el: '#boxerlist',

    data: 
    {
        members: [make_empty_boxer(logindetails.club)],
        year_to_categories: {},
        all_year_categories: []
    },

    created: function () 
    {
        $.get("/api/categories", ( categories ) => 
        {
            for (let category of categories)
            {
                this.year_to_categories[category.year] = category.categories;
            }
            this.all_year_categories = Object.keys(this.year_to_categories);
        });
    },

    methods: 
    {
        send_to_server: function() 
        {
            this.members.forEach(element => {
                element.club = logindetails.club;
            });
            let self = this;
            $.post({ 
                url: "/api/members/update",
                data : JSON.stringify({ members: this.members }),
                contentType : 'application/json',
                beforeSend: beforeSend,
                success: function(data)
                {
                    self.members = [make_empty_boxer(logindetails.club)];
                }
            })
        },

        add_boxer: function(event, index) 
        {
            this.members.push(make_empty_boxer(logindetails.club));
        },

        remove_boxer: function(event, index) 
        {
            this.members.splice(index, 1);
            if (this.members.length == 0)
            {
                this.members = [make_empty_boxer(logindetails.club)];
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


new Vue({

    el: '#navbar',

    data: 
    {
        logindetails    
    },

    methods: 
    {
        switch_to_registration: function()
        {
            $('.page').addClass('hidden');
            $(".page-registration").removeClass('hidden');
        },

        switch_to_all: function()
        {
            $('.page').addClass('hidden');
            $(".page-all").removeClass('hidden');
        },

        switch_to_coach_registration: function()
        {
            $('.page').addClass('hidden');
            $(".page-registration-coach").removeClass('hidden');
        },

        switch_to_login: function()
        {
            $('.page').addClass('hidden');
            $(".page-login").removeClass('hidden');
        }
    }
});


var allVue = new Vue({

    el: '#all',

    data: 
    {
        members: []
    },

    created: function () 
    {
        $.get(
        {
            url: "/api/members", 
            beforeSend: beforeSend,
            success: ( members ) => 
            {
                this.members = members
            }
        });
    },

    methods: 
    {
        download: function() 
        {
            var text = make_excel_file(this.members);
            var filename = "zaregistrovani-boxeri.csv";
            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
            element.setAttribute('download', filename);
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        }        
    }
});


function get_header()
{
    return {
        year_category: 'Ročník', 
        weight_category: 'Kategória',
        n_fights: 'Počet zápasov',
        name: 'Meno a priezvisko',
        club: 'Klub'
    }
}        


function make_excel_file(members)
{
    let separator = ', ';
    let header = get_header();
    let order = ["club", "name", "year_category", "weight_category", "n_fights"];
    let header_line = join_to_line(header, order, separator);
    let lines = [header_line]
    for (member of members)
    {
        lines.push(join_to_line(member, order, separator));
    }
    return lines.join("\n");
}


function join_to_line(object, order, separator)
{
    let strings = [];
    for (property of order)
    {
        strings.push(`"${object[property]}"`);
    }
    return strings.join(separator)
}