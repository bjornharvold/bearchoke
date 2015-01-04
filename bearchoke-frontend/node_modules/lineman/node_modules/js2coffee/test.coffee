b = "b"
c = "c"
d = -> console.log "d"

a = (d()
b + c
)
console.log "retval: #{a}"