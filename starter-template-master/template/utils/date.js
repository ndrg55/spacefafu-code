export function formatDate(date) {
    var dateee = new Date(date).toJSON();
    var fdate = new Date(+new Date(dateee)+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
    return fdate;
};

