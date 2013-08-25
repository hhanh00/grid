$ ->
  "use strict"
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
  socket = $.atmosphere  
  request = {
    url: "/echo"
    transport: "websocket"
    }
  request.onMessage = (response) ->
    console.log(response.responseBody)
  socket.subscribe(request).push("Hello")
  
