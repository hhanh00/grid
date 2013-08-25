$ ->
  $("#grid").jqGrid({
    url: "grid.json"
    datatype: "json"
    colNames: ["price", "quantity"]
    colModel: [ { 
        name: "price",
        index: "price",
        width: 200
      },
      { 
        name: "quantity",
        index: "quantity",
        width: 500
      } ]
    rowNum: 40
    })
