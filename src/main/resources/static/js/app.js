document.addEventListener('DOMContentLoaded', () => {

    if (document.getElementById('usersTableBody')) {
        loadUsers();

        const editForm = document.getElementById('editForm');
        if (editForm) {
            editForm.addEventListener('submit', async e => {
                e.preventDefault();

                const userId = document.getElementById('editUserId').value;
                const formData = new FormData(editForm);
                const roles = Array.from(document.getElementById('editRoles').selectedOptions)
                    .map(option => ({ authority: option.value }));

                const user = {
                    id: userId,
                    name: formData.get('name'),
                    surname: formData.get('surname'),
                    age: +formData.get('age'),
                    email: formData.get('email'),
                    password: formData.get('password') || null,
                    roles: roles
                };

                try {
                    const response = await fetch(`/api/admin/users/${userId}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(user)
                    });
                    if (response.ok) {
                        loadUsers();
                        bootstrap.Modal.getInstance(document.getElementById('editUserModal')).hide();
                    }
                } catch (error) {
                }
            });
        }

        const deleteForm = document.getElementById('deleteForm');
        if (deleteForm) {
            deleteForm.addEventListener('submit', async e => {
                e.preventDefault();
                const userId = document.getElementById('deleteUserId').value;
                try {
                    const response = await fetch(`/api/admin/users/${userId}`, { method: 'DELETE' });
                    if (response.ok) {
                        loadUsers();
                        bootstrap.Modal.getInstance(document.getElementById('deleteUserModal')).hide();
                    }
                } catch (error) {
                }
            });
        }
    }

    async function loadUsers() {
        try {
            const response = await fetch('/api/admin/users');
            const users = await response.json();
            const tbody = document.getElementById('usersTableBody');
            tbody.innerHTML = '';
            users.forEach(user => {
                const rolesString = user.roles.map(r => r.authority.replace('ROLE_', '')).join(', ');
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${rolesString}</td>
                    <td>
                      <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editUserModal"
                        data-id="${user.id}" data-name="${user.name}" data-surname="${user.surname}"
                        data-age="${user.age}" data-email="${user.email}"
                        data-roles="${user.roles.map(r => r.authority).join(',')}">Edit</button>
                    </td>
                    <td>
                      <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteUserModal"
                        data-id="${user.id}" data-name="${user.name}" data-surname="${user.surname}"
                        data-age="${user.age}" data-email="${user.email}"
                        data-roles="${user.roles.map(r => r.authority).join(',')}">Delete</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        } catch (error) {
        }
    }

    if (document.getElementById('userInfoBody')) {
        loadUserInfo();
    }

    async function loadUserInfo() {
        try {
            const response = await fetch('/api/user');
            const user = await response.json();
            const tbody = document.getElementById('userInfoBody');
            tbody.innerHTML = '';
            const rolesString = user.roles.map(r => r.authority.replace('ROLE_', '')).join(', ');
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.surname}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${rolesString}</td>
            `;
            tbody.appendChild(tr);
        } catch (error) {
        }
    }

    const addUserForm = document.getElementById('addUserForm');
    if (addUserForm) {
        addUserForm.addEventListener('submit', async e => {
            e.preventDefault();
            const formData = new FormData(addUserForm);
            const roles = Array.from(document.getElementById('roleSelect').selectedOptions)
                .map(option => ({ authority: option.value }));

            const user = {
                name: formData.get('name'),
                surname: formData.get('surname'),
                age: +formData.get('age'),
                email: formData.get('email'),
                password: formData.get('password'),
                roles: roles
            };

            try {
                const response = await fetch('/api/admin/users', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(user)
                });
            } catch (error) {
            }
        });
    }

});
