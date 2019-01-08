$(() =>
{
    $.get( "clubs", ( clubs ) =>
    {
        let datalist = $('#allclubs');
        for (club of clubs) 
        {
            datalist.append(new Option(club));
        }
    });
});