$(function(){

    $.get( "clubs", function( clubs ) {

        clubs.array.forEach(element => {
            $('#club').append( new Option(element) );
        });
    });
    
});