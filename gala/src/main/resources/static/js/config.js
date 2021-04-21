myserver = {
    // api: 'http://localhost:1993',
    api: 'http://www.gala.zone',
    resources: {
      agentDownload: 'https://download.aiops.com',
      api: 'http://api.aiops.com',
      mail: 'camail.aiops.com'
    }


}


function htmlJump(jump) {
    window.location.href=myserver.api+jump;
}