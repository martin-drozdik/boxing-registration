$(() =>
{

    $.get( "clubs", ( clubs ) =>
    {
        let datalist = $('#club');
        for (club of clubs) 
        {
            datalist.append(new Option(club));
        }
    });
    
});