rm -r kube
mkdir kube
kompose -f docker-compose_kompose.yml convert --out kube