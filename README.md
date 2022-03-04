# Romedawg.com

# playgroud for roman

# how to deploy?

# what components are needed?
 - postgres server

export POSTGRES_HOST=localhost
export POSTGRES_DATABASE=romedawg
export POSTGRES_USERNAME=postgres
export POSTGRES_PASSWORD=password

METRA_URL_USERNAME=f1f57661005181a1238b7d958fdb889f
METRAL_URL_PASSWORD=e9152c2ae51643ca236a3521d46d4f39


aws ec2 describe-instances \
--filters "Name=tag:application,Values=mysql" "Name=tag:cluster,Values=$CLUSTER" "Name=tag:backup_create,Values=false" "Name=tag:environment,Values=$ENV"  \
--query 'Reservations[*].Instances[*].{IpAddress:PrivateIpAddress,Instance:InstanceId,AZ:Placement.AvailabilityZone,Name:Tags[?Key==`Name`]|[0].Value,ENV:Tags[?Key==`environment`]|[0].Value,backup_host:Tags[?Key==`backup_create`]|[0].Value}' \
--output table
