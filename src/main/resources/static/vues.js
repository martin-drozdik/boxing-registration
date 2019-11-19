let current_tournament = { name: "" }


let logindetails = { username: "", password: "", club: "" };

function make_empty_boxer(club) 
{
    return { year_category: '', weight_category: '', n_fights: '', name: '', club: club, tournament_name: current_tournament.name };
}


function makeBeforeSend(logindetails)
{
    return function (xhr) 
    {
        xhr.setRequestHeader ("Authorization", "Basic " + btoa(logindetails.username + ":" + logindetails.password));
    }
}


function login(vue, club, email, password, loggedin_user)
{
    vue.errors = [];
    logindetails.club = club;
    logindetails.username = email;
    logindetails.password = password;

    $('.page').addClass('hidden');

    if (loggedin_user.admin)
    {
        $("#tournament-nav").removeClass('hidden');
        $(".page-tournament").removeClass('hidden');
    }
    else
    {
        $("#registration-nav").removeClass('hidden');
        $(".page-registration").removeClass('hidden');
    }
    
    $("#login-nav").addClass('hidden');
    $("#user-registration-nav").addClass('hidden');
    $("#all-nav").removeClass('hidden');
    
    let beforeSend = makeBeforeSend(logindetails);

    $.get(
    {
        url: "/api/members", 
        beforeSend,
        success: ( members ) => 
        {
            allVue.members = members;
        }
    });

    $.get(
    {
        url: "/api/tournament", 
        beforeSend,
        success: ( data ) => 
        {
            current_tournament.name = data;
        }
    });

    console.log(`Club changed to ${logindetails.club}`)
}


new Vue({

    el: '#coach-registration',

    data: {
        coach: { name: "", email: "", password: "", club: "" },
        all_clubs: [],
        errors: []
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
            let self = this;
            $.post({ 
                url: "/api/register",
                data : JSON.stringify(this.coach),
                contentType : 'application/json',
                success: function(registered_user)
                {
                    $('.page').addClass('hidden');
                    $(".page-login").removeClass('hidden');
                    login(self, self.coach.club, self.coach.email, self.coach.password, registered_user)
                },
                error: function(data) 
                {
                    self.errors = [data.responseJSON.message];
                }
            })
        }
    }
});


var loginVue = new Vue({

    el: '#login',

    data:
    {
        logindetails: { username: "", password: "", club: "" },
        errors: []
    },

    methods: 
    {
        send_to_server: function()
        {
            let self = this;
            let beforeSend = makeBeforeSend(self.logindetails);
            $.get({ 
                url: "/api/login",
                contentType : 'application/json',
                beforeSend,
                success: function(registered_user)
                {                   
                    login(self, self.logindetails.club, self.logindetails.username, self.logindetails.password, registered_user)
                },
                error: function(data) 
                {
                    if (data.status == 401)
                    {
                        self.errors = ["Nesprávne heslo, alebo nesprávny e-mail."];
                    }
                    else
                    {
                        self.errors = ["Stala sa chyba. Neznamená to, že ste zadali nesprávne heslo alebo e-mail. Prosím kontaktujte administrátora stránky."];
                    }
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
        current_tournament,
        year_to_categories: {},
        all_year_categories: []
    },

    computed: 
    {
        is_disabled: function () 
        {
            return this.current_tournament.name == "";
        }
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
                beforeSend: makeBeforeSend(logindetails),
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
        },

        get_no_tournament_message: function()
        {
            return "Nie je vyhlásený žiaden turnaj";
        },

        get_tournament_message: function()
        {
            if (current_tournament.name == "")
                return this.get_no_tournament_message() 
            else 
                return this.current_tournament.name;
        }
    }
});


var allVue = new Vue({

    el: '#all',

    data: 
    {
        members: [],
        current_tournament
    },

    methods: 
    {
        refresh: function()
        {
            let self = this;
            $.get({ 
                url: "/api/members",
                beforeSend: makeBeforeSend(logindetails),
                success: function(data)
                {
                    self.members = data
                }});
        },

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
    },

    created ()
    {
        this.refresh();
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
            allVue.refresh();
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
        },

        switch_to_tournament: function()
        {
            $('.page').addClass('hidden');
            $(".page-tournament").removeClass('hidden');
        }
    }
});





var tournamentVue = new Vue({

    el: '#tournament',

    data: 
    {
        current_tournament,
        new_tournament: ""
    },

    methods: 
    {
        send_to_server: function() 
        {
            let self = this;
            $.post({ 
                url: "/api/newtournament",
                data : JSON.stringify({ name: this.new_tournament }),
                contentType : 'application/json',
                beforeSend: makeBeforeSend(logindetails),
                success: function(data)
                {
                    current_tournament.name = self.new_tournament;
                    new_tournament = "";
                }
            })
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